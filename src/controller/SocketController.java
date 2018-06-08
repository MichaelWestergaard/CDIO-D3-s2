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

import datalag.MySQLController;
import datalag.ProductBatchDTO;
import datalag.ReceptComponentDTO;
import datalag.UserDTO;
import datalag.IngBatchDTO;
import datalag.ReceptDTO;


public class SocketController implements Runnable {
	Socket socket;
	static String readLine = null;
	MySQLController mySQLController;
	int operatorID;
	int productBatchID;
	int ingredientBatchID;
	double tara;
	double netto;

	public SocketController() throws ClassNotFoundException {
		mySQLController = new MySQLController();
	}

	public void init() {
		try {
			socket = new Socket("169.254.2.3", 8000);
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
		double loadValue = Double.parseDouble(new StringBuilder().append(readChar[8]).append(readChar[9]).append(readChar[10]).append(readChar[11]).append(readChar[12]).toString());
		return loadValue;
	}

	public double getLoadFromString(String loadString) {
		char[] loadChar = loadString.toCharArray();
		System.out.println(loadChar.length);
		double loadValue = Double.parseDouble(new StringBuilder().append(loadChar[9]).append(loadChar[10]).append(loadChar[11]).append(loadChar[12]).toString());
//		String[] loadArr = loadString.split(" ");
//		double loadValue = Double.parseDouble(loadArr[2]);
		return loadValue;
	}

	public void loginProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			sendMessage("RM20 8 \"Indtast dit ID:\" \"\" \"&3\"");

			boolean userConfirmed = false;
			while(!userConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				int input = Integer.parseInt(inputArr[2].replace("\"", ""));
				UserDTO user = mySQLController.getUser(input);
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
						} else {
							sendMessage("RM20 8 \"Indtast ID igen:\" \"\" \"&3\"");
						}
					} else {
						sendMessage("RM20 8 \"Kun laboranter tilladt!:\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"Forkert ID! Proev igen\" \"\" \"&3\"");
				}
			}			
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		}
	}

	public void ingredientProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			
			sendMessage("RM20 8 \"Indtast Raavare ID:\" \"\" \"&3\"");
			boolean ingredientConfirmed = false;
			
			while(!ingredientConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				ReceptComponentDTO recept = null;
				int ingredientID = Integer.parseInt(inputArr[2].replace("\"", ""));
				List<ReceptComponentDTO> receptComponents = mySQLController.getReceptComponents(mySQLController.getProductBatch(productBatchID).getReceptID());
				
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
					boolean nothingLeft = false;
					int nothingLeftCount = 0;
					
					List<Integer> ingredientBatchesByIngredient = mySQLController.getIngredientBatchesByIngredient(ingredientID);
					for (Iterator iterator = ingredientBatchesByIngredient.iterator(); iterator.hasNext();) {
						Integer ingredientBatchID = (Integer) iterator.next();
						
						if(mySQLController.getIngBatch(ingredientBatchID).getAmount() >= recept.getNomNetto()) {
							//Tjek om råvaren er blevet afvejet før
							if(mySQLController.getProductBatchComponent(productBatchID, ingredientBatchID) == null) {
								availableIngredientBatches.add(ingredientBatchID);
								availableIngredientBatchesText += Integer.toString(ingredientBatchID);
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
							sendMessage("RM20 8 \"Indtast RB ID ("+availableIngredientBatchesText+")\" \"\" \"&3\"");
							inputString = reader.readLine();
							inputArr = inputString.split(" ");
							sleep();
							int input = Integer.parseInt(inputArr[2].replace("\"", ""));
							if(availableIngredientBatches.contains(input)) {
								if(mySQLController.getIngBatch(ingredientBatchID).getAmount() >= recept.getNomNetto()) {
									this.ingredientBatchID = ingredientBatchID;
									ingredientConfirmed = true;
								} else {
									sendMessage("RM20 8 \"Ikke nok maengde, proev igen\" \"\" \"&3\"");
								}
							} else {
								sendMessage("RM20 8 \"Forkert RB ID ("+availableIngredientBatchesText+")\" \"\" \"&3\"");
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
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ingredientBatchProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			sendMessage("RM20 8 \"Indtast raavarebatch ID:\" \"\" \"&3\"");
			boolean ingredientBatchConfirmed = false;
			while(!ingredientBatchConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				int input = Integer.parseInt(inputArr[2].replace("\"", ""));
				IngBatchDTO ingredientBatch = mySQLController.getIngBatch(input);
				boolean ingredientInRecept = false;
				int receptID = (mySQLController.getProductBatch(productBatchID)).getReceptID();
				for(ReceptComponentDTO receptComponent : mySQLController.getReceptComponents()) {
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
				} else {
					sendMessage("RM20 8 \"ID'et findes ikke\" \"\" \"&3\"");
				}
			}			
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		}
	}

	public void nettoProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String ingredientName = mySQLController.getIngredient(mySQLController.getIngBatch(ingredientBatchID).getIngredientID()).getIngredientName();
			sendMessage("RM20 8 \"Afvej " + ingredientName + ":\" \"\" \"&3\"");
			boolean nettoConfirmed = false;
			while(!nettoConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();

				if(inputArr[1].equals("A")) {
					netto = getLoad();

					int ingredientID = (mySQLController.getIngBatch(ingredientBatchID)).getIngredientID();
					int receptID = (mySQLController.getProductBatch(productBatchID)).getReceptID();
					double nomNetto = (mySQLController.getReceptComponent(receptID, ingredientID)).getNomNetto();
					double tolerance = (mySQLController.getReceptComponent(receptID, ingredientID)).getTolerance();

					if(netto >= nomNetto - nomNetto*tolerance && netto <= nomNetto + nomNetto*tolerance) {
						nettoConfirmed = true;
						mySQLController.createProductBatchComponent(productBatchID, ingredientBatchID, operatorID, netto, tara);
						sendMessage("T");
						sleep();	
					} else {
						sendMessage("RM20 8 \"Tolerance overholdes ikke\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \" Proev igen!\" \"\" \"&3\"");
				}
			}
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		}		
	}

	public void batchProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			sendMessage("RM20 8 \"" + "Indsaet batchID" + "\" \"\" \"&3\"");

			boolean batchConfirmed = false;
			while(!batchConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				int input = Integer.parseInt(inputArr[2].replace("\"", ""));
				ProductBatchDTO productBatch = mySQLController.getProductBatch(input);
				if(	productBatch != null) {
					if(productBatch.getStatus() != 2) {
						sendMessage("RM20 8 \"" + "Bekraeft " + mySQLController.getRecept(productBatch.getReceptID()).getReceptName() + "?" + "\" \"\" \"&3\"");
	
						productBatchID = input;
	
						inputString = reader.readLine();
						inputArr = inputString.split(" ");
	
						if(inputArr[1].equals("A")) {
							batchConfirmed = true;
						} else {
							sendMessage("RM20 8 \"" + "Preov nyt batchID" + "\" \"\" \"&3\"");
						}
					} else {
						sendMessage("RM20 8 \"" + "Produktionen er afsluttet!" + "\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"" + "Ikke fundet! proev igen" + "\" \"\" \"&3\"");
				}
			}

		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		}	catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		}
	}

	public void unloadProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			sendMessage("RM20 8 \"" + "Er vaegten ubelastet?" + "\" \"\" \"&3\"");

			boolean unloadConfirmed = false;
			while(!unloadConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();

				if(inputArr[1].equals("A")) {
					unloadConfirmed = true;
					sendMessage("T");

				} else {
					sendMessage("RM20 8 \"Fjern vaegten og bekraeft" + "U?" + "\" \"\" \"&3\"");
				}
			}

		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		}
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
					if(Character.isAlphabetic(c)) {
						alphaCount++;
						if(alphaCount < 20) {
							returnMsg += c;
						} else {
							if(specialCount+2 < 8) {
								returnMsg += "..";
								break;
							}
						}
					} else if(!Character.isDigit(c) && !Character.isLetter(c)) {
						specialCount++;
						if(specialCount < 8) {
							returnMsg += c;
						} else {
							if(specialCount+2 < 8) {
								returnMsg += "..";
								break;
							}
						}
					}
				}
			}
			
			pw.println(returnMsg);
			pw.flush();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void taraProcedure() {
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
					taraConfirmed = true;
					sendMessage("T");

					sleep();
					tara = getLoadFromString(readLine);

				} else {
					msg = "Proev igen og godkend";
					sendMessage("RM20 8 \"" + msg + "\" \"\" \"&3\"");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void completeProcedure() {
		loginProcedure();
		batchProcedure();
		unloadProcedure();
		taraProcedure();
		ingredientProcedure();
		nettoProcedure();
		sleep();
	}

}