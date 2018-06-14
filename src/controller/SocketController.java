package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import DAO.IngBatchDAO;
import DAO.IngredientDAO;
import DAO.ProductBatchComponentDAO;
import DAO.ProductBatchDAO;
import DAO.ReceptComponentDAO;
import DAO.ReceptDAO;
import DAO.UserDAO;
import DAO.BaseDAO.NotImplementedException;
import DTO.IngBatchDTO;
import DTO.ProductBatchComponentDTO;
import DTO.ProductBatchDTO;
import DTO.ReceptComponentDTO;
import DTO.UserDTO;


public class SocketController implements Runnable {
	Socket socket;
	static String readLine = null;
	
	UserDAO userDAO;
	IngBatchDAO ingBatchDAO;
	IngredientDAO ingredientDAO;
	ProductBatchDAO productBatchDAO;
	ProductBatchComponentDAO productBatchComponentDAO;
	ReceptComponentDAO receptComponentDAO;
	ReceptDAO receptDAO;
	
	int operatorID;
	int productBatchID;
	int ingredientBatchID;
	double tara;
	double netto;

	public SocketController() throws ClassNotFoundException {
		userDAO = new UserDAO();
		ingBatchDAO = new IngBatchDAO();
		ingredientDAO = new IngredientDAO();
		productBatchDAO = new ProductBatchDAO();
		productBatchComponentDAO = new ProductBatchComponentDAO();
		receptComponentDAO = new ReceptComponentDAO();
		receptDAO = new ReceptDAO();
	}

	public void init() {
		try {
			socket = new Socket("169.254.2.3", 8000);
			socket.setKeepAlive(true);
			socket.setSoTimeout(0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while(true) {
				readLine = bufferedReader.readLine();
				System.out.println(readLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sleep() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public double getLoad() {
		sendMessage("S");
		sleep();		
		char[] readChar = readLine.toCharArray();
		double loadValue = Double.parseDouble(new StringBuilder().append(readChar[8]).append(readChar[9]).append(readChar[10]).append(readChar[11]).append(readChar[12]).append(readChar[13]).toString());
		return loadValue;
	}

	public double getLoadFromString(String loadString) {
		char[] loadChar = loadString.toCharArray();
		System.out.println(loadChar.length);
		double loadValue = Double.parseDouble(new StringBuilder().append(loadChar[9]).append(loadChar[10]).append(loadChar[11]).append(loadChar[12]).append(loadChar[13]).toString());
		//		String[] loadArr = loadString.split(" ");
		//		double loadValue = Double.parseDouble(loadArr[2]);
		return loadValue;
	}

	public boolean loginProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			sendMessage("RM20 8 \"Indtast dit ID:\" \"\" \"&3\"");

			boolean userConfirmed = false;
			while(!userConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				if(inputArr.length >= 3) {
					int input = Integer.parseInt(inputArr[2].replace("\"", ""));
					UserDTO user = userDAO.read(input);
					if(user != null) {
	
						boolean allowed = false;
						//Tjek om brugeren har rollen "Laborant"
						for (String role : user.getRole()) {
							if(role.equals("Laborant") && !allowed) {
								allowed = true;
							}
						}
	
						if(allowed) {
							sendMessage("RM20 8 \"Er du " + user.getInitial() + "?\" \"\" \"&3\"");
	
							inputString = reader.readLine();
							inputArr = inputString.split(" ");
	
							if(inputArr[1].equals("A")) {
								operatorID = user.getUserID();
								userConfirmed = true;
								return true;
							} else {
								sendMessage("RM20 8 \"Indtast ID igen:\" \"\" \"&3\"");
							}
						} else {
							sendMessage("RM20 8 \"Kun laboranter tilladt!:\" \"\" \"&3\"");
						}
					} else {
						sendMessage("RM20 8 \"Forkert ID! Proev igen\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"Indtast ID igen:\" \"\" \"&3\"");
				}
			}			
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (NumberFormatException e) {
			sendMessage("RM20 8 \"Indtast dit ID:\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		} catch (ClassNotFoundException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getMessage()+"\" \"\" \"&3\"");
		}
		return false;
	}

	public boolean ingredientProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			sendMessage("RM20 8 \"Indtast Raavare ID:\" \"\" \"&3\"");
			boolean ingredientConfirmed = false;

			while(!ingredientConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				
				boolean continueProcedure = false;
				
				if(inputArr[1].equals("A")) {
					if(inputArr.length == 2) {
						continueProcedure = true;
					} else if(inputArr.length == 3) {
						if(!inputArr[2].replace("\"", "").equals("")) {
							if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
								return false;
							}
							
							continueProcedure = true;
						} else {
							continueProcedure = false;
						}
					} else {
						continueProcedure = false;
					}
				} else {
					continueProcedure = false;
				}
				
				if(continueProcedure) {
					ReceptComponentDTO recept = null;
					int ingredientID = Integer.parseInt(inputArr[2].replace("\"", ""));
					List<ReceptComponentDTO> receptComponents = receptComponentDAO.list(productBatchDAO.read(productBatchID).getReceptID());
	
					boolean ingredientInRecept = false;
					for (ReceptComponentDTO receptComponentDTO : receptComponents) {
						if(receptComponentDTO.getIngredientID() == ingredientID) {
							recept = receptComponentDTO;
							ingredientInRecept = true;
						}
					}
	
					if(ingredientInRecept) {
						//Find mulige RB id'er
						List<Integer> availableIngredientBatches = new ArrayList<Integer>();
						String availableIngredientBatchesText = "";
						boolean alreadyWeighed = false;
						int nothingLeftCount = 0;
	
						List<Integer> ingredientBatchesByIngredient = ingBatchDAO.list(ingredientID);
						for (Iterator<Integer> iterator = ingredientBatchesByIngredient.iterator(); iterator.hasNext();) {
							Integer ingredientBatchID = (Integer) iterator.next();
	
							if(ingBatchDAO.read(ingredientBatchID).getAmount() >= recept.getNomNetto()) {
								//Tjek om råvaren er blevet afvejet før
								if(productBatchComponentDAO.read(productBatchID, ingredientBatchID) == null) {
									availableIngredientBatches.add(ingredientBatchID);
									availableIngredientBatchesText += "" + ingredientBatchID;
									if(iterator.hasNext()) {
										availableIngredientBatchesText += ",";
									}
								} else {
									//råvaren/råvarebatchen er blevet afvejet
									alreadyWeighed = true;
									//TODO: break måske..
								}
							} else {
								//Ikke nok mængde
								nothingLeftCount++;
							}
						}
						
						if(nothingLeftCount == ingredientBatchesByIngredient.size()) {
							sendMessage("RM20 8 \"Ikke nok på lager!\" \"\" \"&3\"");
							//TODO: Hvad skal der så ske?
						} else {
							if(!alreadyWeighed) {
								sendMessage("RM20 8 \"Indtast RB ID (" + availableIngredientBatchesText.toString() + ")\" \"\" \"&3\"");
								inputString = reader.readLine();
								inputArr = inputString.split(" ");
								sleep();
								
								boolean inputOK = false;
								
								if(inputArr[1].equals("A")) {
									if(inputArr.length == 2) {
										inputOK = true;
									} else if(inputArr.length == 3) {
										if(!inputArr[2].replace("\"", "").equals("")) {
											if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
												return false;
											}
											
											inputOK = true;
										} else {
											inputOK = false;
										}
									} else {
										inputOK = false;
									}
								} else {
									inputOK = false;
								}
								
								if(inputOK) {
									int ingredientBatchID = Integer.parseInt(inputArr[2].replace("\"", ""));
									if(availableIngredientBatches.contains(ingredientBatchID)) {
										if(ingBatchDAO.read(ingredientBatchID).getAmount() >= recept.getNomNetto()) {
											this.ingredientBatchID = ingredientBatchID;
											ingredientConfirmed = true;
											return true;
										} else {
											sendMessage("RM20 8 \"Ikke nok maengde, proev igen\" \"\" \"&3\"");
										}
									} else {
										sendMessage("RM20 8 \"Forkert RB ID ("+availableIngredientBatchesText+")\" \"\" \"&3\"");
									}
								} else {
									sendMessage("RM20 8 \"Proev igen!\" \"\" \"&3\"");
								}
							} else {
								sendMessage("RM20 8 \"Raavare er afvejet!\" \"\" \"&3\"");
								ingredientConfirmed = false;
							}
						}
					} else {
						sendMessage("RM20 8 \"Forkert raavareID, proev igen.\" \"\" \"&3\"");
						ingredientConfirmed = false;
					}
				} else {
					sendMessage("RM20 8 \"Proev igen!\" \"\" \"&3\"");
				}
			}

		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		} catch (ClassNotFoundException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getMessage()+"\" \"\" \"&3\"");
		}
		return false;
	}

	public boolean ingredientBatchProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			sendMessage("RM20 8 \"Indtast raavarebatch ID:\" \"\" \"&3\"");
			boolean ingredientBatchConfirmed = false;
			while(!ingredientBatchConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				boolean continueProcedure = false;
				
				if(inputArr[1].equals("A")) {
					if(inputArr.length == 2) {
						continueProcedure = true;
					} else if(inputArr.length == 3) {
						if(!inputArr[2].replace("\"", "").equals("")) {
							if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
								return false;
							}
							
							continueProcedure = true;
						} else {
							continueProcedure = false;
						}
					} else {
						continueProcedure = false;
					}
				} else {
					continueProcedure = false;
				}
				
				if(continueProcedure) {
					int input = Integer.parseInt(inputArr[2].replace("\"", ""));
					IngBatchDTO ingredientBatch = ingBatchDAO.read(input);
					boolean ingredientInRecept = false;
					int receptID = (productBatchDAO.read(productBatchID)).getReceptID();
					for(ReceptComponentDTO receptComponent : receptComponentDAO.list()) {
						if(receptComponent.getReceptID() == receptID) {
							if(ingredientBatch != null) {
								if(receptComponent.getIngredientID() == ingredientBatch.getIngredientID()) {
									ingredientInRecept = true;
								}
							}
						}	
					}
					
					if(ingredientInRecept) {
						ingredientBatchID = ingredientBatch.getIngBatchID();
						ingredientBatchConfirmed = true;
						return true;
					} else {
						sendMessage("RM20 8 \"ID'et findes ikke\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"Proev igen!\" \"\" \"&3\"");
				}
			}			
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		} catch (ClassNotFoundException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getMessage()+"\" \"\" \"&3\"");
		}
		return false;
	}

	public boolean nettoProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String ingredientName = ingredientDAO.read(ingBatchDAO.read(ingredientBatchID).getIngredientID()).getIngredientName();
			sendMessage("RM20 8 \"Afvej " + ingredientName + ":\" \"\" \"&3\"");
			boolean nettoConfirmed = false;
			while(!nettoConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				boolean continueProcedure = false;
				
				if(inputArr[1].equals("A")) {
					if(inputArr.length == 2) {
						continueProcedure = true;
					} else if(inputArr.length == 3) {
						if(!inputArr[2].replace("\"", "").equals("")) {
							if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
								return false;
							}
							
							continueProcedure = true;
						} else {
							continueProcedure = false;
						}
					} else {
						sendMessage("RM20 8 \"" + "Afvejningsfejl" + "\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"" + "Afvejningsfejl" + "\" \"\" \"&3\"");
				}
				
				if(continueProcedure) {
					netto = getLoad();

					int ingredientID = (ingBatchDAO.read(ingredientBatchID)).getIngredientID();
					int receptID = (productBatchDAO.read(productBatchID)).getReceptID();
					ReceptComponentDTO receptComponent = receptComponentDAO.read(receptID, ingredientID);
					double nomNetto = receptComponent.getNomNetto();
					double tolerance = receptComponent.getTolerance();
					System.out.println("Netto: " + netto);
					System.out.println("Min: " + (nomNetto - (nomNetto*tolerance)/100));
					System.out.println("Max: " + (nomNetto + (nomNetto*tolerance)/100));
					if(netto >= (nomNetto - (nomNetto*tolerance)/100) && netto <= (nomNetto + (nomNetto*tolerance)/100)) {
						nettoConfirmed = true;
						if(ingBatchDAO.updateAmount(ingredientBatchID, netto)) {
							productBatchComponentDAO.create(new ProductBatchComponentDTO(productBatchID, ingredientBatchID, 0, null, operatorID, null, netto, tara));
							System.out.println("Done");
							sendMessage("T");
							sleep();
							return true;
						} else {
							sendMessage("RM20 8 \"Mere afvejet end i PB'en\" \"\" \"&3\"");
						}
					} else {
						System.out.println("fejl");
						//TODO Fix tekst
						sendMessage("RM20 8 \"Fejl i afvejning\" \"\" \"&3\"");
					}
				} 
			}
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			e.printStackTrace();
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		} catch (ClassNotFoundException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getMessage() + "!\" \"\" \"&3\"");
		} catch (NotImplementedException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getMessage() + "!\" \"\" \"&3\"");
		}
		return false;
	}

	public boolean batchProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			sendMessage("RM20 8 \"" + "Indsaet batchID" + "\" \"\" \"&3\"");

			boolean batchConfirmed = false;
			while(!batchConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				boolean inputOK = false;
				System.out.println(inputString);
				
				if(inputArr[1].equals("A")) {
					if(inputArr.length == 2) {
						inputOK = true;
					} else if(inputArr.length == 3) {
						if(!inputArr[2].replace("\"", "").equals("")) {							
							if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
								return false;
							}
							
							inputOK = true;
						} else {
							inputOK = false;
						}
					} else {
						inputOK = false;
					}
				} else {
					inputOK = false;
				}
				
				if(inputOK) {
					int input = Integer.parseInt(inputArr[2].replace("\"", ""));
					ProductBatchDTO productBatch = productBatchDAO.read(input);
					if(	productBatch != null) {
						if(productBatch.getStatus() != 2) {
							sendMessage("RM20 8 \"" + "Bekraeft " + receptDAO.read(productBatch.getReceptID()).getReceptName() + "?" + "\" \"\" \"&3\"");
	
							productBatchID = input;
	
							inputString = reader.readLine();
							inputArr = inputString.split(" ");
							if(inputArr[1].equals("A")) {
								if(inputArr.length == 2) {
									batchConfirmed = true;
									return true;
								} else if(inputArr.length == 3) {
									if(!inputArr[2].replace("\"", "").equals("")) {
										if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
											return false;
										}
									} else {
										batchConfirmed = true;
										return true;
									}
								} else {
									sendMessage("RM20 8 \"" + "Preov nyt batchID" + "\" \"\" \"&3\"");
								}
							} else {
								sendMessage("RM20 8 \"" + "Preov nyt batchID" + "\" \"\" \"&3\"");
							}
						} else {
							sendMessage("RM20 8 \"" + "Afsluttet, proev igen!" + "\" \"\" \"&3\"");
						}
					} else {
						sendMessage("RM20 8 \"" + "Ikke fundet! proev igen" + "\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"" + "Proev igen" + "\" \"\" \"&3\"");
				}
			}
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		} catch (ClassNotFoundException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getMessage() + "!\" \"\" \"&3\"");
		}
		return false;
	}

	public boolean unloadProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			sendMessage("RM20 8 \"" + "Er vaegten ubelastet?" + "\" \"\" \"&3\"");

			boolean unloadConfirmed = false;
			while(!unloadConfirmed) {
				String inputString = reader.readLine();
				System.out.println(inputString);
				String[] inputArr = inputString.split(" ");
				sleep();
				
				if(inputArr[1].equals("A")) {
					if(inputArr.length == 2) {
						unloadConfirmed = true;
						sendMessage("T");
						return true;
					} else if(inputArr.length == 3) {
						if(!inputArr[2].replace("\"", "").equals("")) {
							if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
								return false;
							}
							
							unloadConfirmed = true;
						} else {
							unloadConfirmed = true;
							sendMessage("T");
							return true;
						}
					} else {
						sendMessage("RM20 8 \"Fjern vaegten og bekraeft?\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"Fjern vaegten og bekraeft?\" \"\" \"&3\"");
				}
			}

		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		}
		return false;
	}

	public void sendMessage(String msg) {
		try {
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);

			//Tjek om beskeden ikke har for mange tegn
			String[] msgArray = msg.split(" ");
			String returnMsg = "";
			if(msgArray[0].equals("RM20") && msgArray[1].equals("8")) {
				String realMsg = msg.substring(msg.indexOf("8")+3, msg.indexOf("\" \"\" \"&3\""));
				char[] chars = realMsg.toCharArray();

				int alphaCount = 0, specialCount = 0;

				for (char c : chars) {
					if(alphaCount+specialCount < 22) {
						if(Character.isAlphabetic(c) || Character.isDigit(c) || c == ',') {
							if(alphaCount < 20) {
								returnMsg += c;
								alphaCount++;
							} else {
								if(specialCount+2 < 8) {
									returnMsg += "..";
									break;
								}
							}
						} else if(!Character.isDigit(c) && !Character.isLetter(c)) {
							if(specialCount < 8) {
								returnMsg += c;
								specialCount++;
							} else {
								if(specialCount+2 < 8) {
									returnMsg += "..";
									break;
								}
							}
						}
					} else {
						if(specialCount+2 < 8) {
							returnMsg += "..";
						}
						break;
					}
				}
				System.out.println(returnMsg);
				returnMsg = "RM20 8 \"" + returnMsg + "\" \"\" \"&3\"";

				System.out.println(returnMsg);
				pw.println(returnMsg);
				pw.flush();	
			} else {		
				pw.println(msg);
				pw.flush();	
			}
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		}
	}

	
	public boolean taraProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String msg = "Placer tara";
			sendMessage("RM20 8 \"" + msg + "\" \"\" \"&3\"");


			boolean taraConfirmed = false;
			while(!taraConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				
				if(inputArr[1].equals("A")) {
					if(inputArr.length == 2) {
						taraConfirmed = true;
						sendMessage("T");

						sleep();
						tara = getLoadFromString(readLine);
						return true;
					} else if(inputArr.length == 3) {
						if(!inputArr[2].replace("\"", "").equals("")) {
							if(!Character.isDigit(inputArr[2].replace("\"", "").charAt(0))){
								return false;
							}
							
							taraConfirmed = true;
						} else {
							taraConfirmed = true;
							sendMessage("T");

							sleep();
							tara = getLoadFromString(readLine);
							return true;
						}
					} else {
						msg = "Proev igen og godkend";
						sendMessage("RM20 8 \"" + msg + "\" \"\" \"&3\"");
					}
				} else {
					msg = "Proev igen og godkend";
					sendMessage("RM20 8 \"" + msg + "\" \"\" \"&3\"");
				}
			}

		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		}
		return false;
	}

	public void completeProcedure() {
		
		outerloop:
		while(true) {
			try {

				if(!loginProcedure()) {
					break outerloop;
				}
				if(!batchProcedure()) {
					break outerloop;
				}

				//Antal råvare der skal afvejes
				int numberOfReceptComponents = 0;
				int receptID = (productBatchDAO.read(productBatchID)).getReceptID();
				for(ReceptComponentDTO receptComponent : receptComponentDAO.list()) {
					if(receptComponent.getReceptID() == receptID) {
						numberOfReceptComponents++;
					}
				}

				//Antal råvare der allerede er afvejet
				int numberOfPBComponents = 0;
				for(ProductBatchComponentDTO productBatchComponent : productBatchComponentDAO.list()) {
					if(productBatchComponent.getIngredientBatchID() == ingredientBatchID) {
						numberOfPBComponents++;
					}
				}
				
				System.out.println(numberOfReceptComponents - numberOfPBComponents);

				for(int i = 0; i < numberOfReceptComponents - numberOfPBComponents; i++) {
					if(!unloadProcedure()) {
						break outerloop;
					}
					if(!taraProcedure()) {
						break outerloop;
					}
					if(!ingredientProcedure()) {
						break outerloop;
					}
					if(!nettoProcedure()) {
						break outerloop;
					}
				}

				//Afsluttet afvejning besked
				if(productBatchDAO.finishProductBatch(productBatchID)) {
					InputStream is = socket.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));

					String msg = "Afvejning afsluttet";
					sendMessage("RM20 8 \"" + msg + "\" \"\" \"&3\"");

					boolean finishConfirmed = false;
					while(!finishConfirmed) {
						String inputString = reader.readLine();
						String[] inputArr = inputString.split(" ");
						sleep();

						if(inputArr[1].equals("A")) {
							finishConfirmed = true;
							sleep();
						} else {
							msg = "Tryk OK";
							sendMessage("RM20 8 \"" + msg + "\" \"\" \"&3\"");
						}
					}

				}

				sleep();
			} catch (SQLException e) {
				sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
			} catch (IOException e) {
				sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
			} catch (ClassNotFoundException e) {
				sendMessage("RM20 8 \"Fejl: "+e.getMessage() + "!\" \"\" \"&3\"");
			}

		}
	}

}