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
import java.util.concurrent.TimeUnit;

import datalag.MySQLController;
import datalag.ProductBatchDTO;
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
			socket = new Socket("169.254.2.2", 8000);
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
//			char[] loadChar = loadString.toCharArray();
//			double loadValue = Double.parseDouble(new StringBuilder().append(loadChar[9]).append(loadChar[10]).append(loadChar[11]).append(loadChar[12]).toString());
			String[] loadArr = loadString.split(" ");
			double loadValue = Double.parseDouble(loadArr[2]);
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
					if(inputArr[1].equals("A")) {
						operatorID = user.getUserID();
						userConfirmed = true;
					} else {
						sendMessage("RM20 8 \"Indtast ID igen:\" \"\" \"&3\"");
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
	
	public void ingredientBatchProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			
			sendMessage("RM20 8 \"Indtast råvarebatch ID:\" \"\" \"&3\"");
			
			boolean ingredientBatchConfirmed = false;
			while(!ingredientBatchConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				int input = Integer.parseInt(inputArr[2].replace("\"", ""));
				IngBatchDTO ingredientBatch = mySQLController.getIngBatch(input);
				if(ingredientBatch != null) {	
					ingredientBatchID = ingredientBatch.getIngBatchID();
					ingredientBatchConfirmed = true;
				} else {
					sendMessage("RM20 8 \"Råvarebatch ID'et findes ikke\" \"\" \"&3\"");
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
			
			sendMessage("RM20 8 \"Afvej råvaren:\" \"\" \"&3\"");
	
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
						sendMessage("T");
						sleep();
						System.out.println("tara success");		
					} else {
						sendMessage("RM20 8 \"Tolerance overholdes ikke\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \" Prøv igen!\" \"\" \"&3\"");
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
			sendMessage("RM20 8 \"" + "Inds�t batchID" + "\" \"\" \"&3\"");
			
			boolean batchConfirmed = false;
			while(!batchConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				int input = Integer.parseInt(inputArr[2].replace("\"", ""));
				ProductBatchDTO productBatch = mySQLController.getProductBatch(input);
				if(	productBatch != null) {					
					sendMessage("RM20 8 \"" + "Bekr�ft " + mySQLController.getRecept(productBatch.getReceptID()).getReceptName() + "?" + "\" \"\" \"&3\"");

					productBatchID = input;
					
					inputString = reader.readLine();
					inputArr = inputString.split(" ");

					if(inputArr[1].equals("A")) {
						batchConfirmed = true;
					} else {
						sendMessage("RM20 8 \"" + "Preov nyt batchID" + "\" \"\" \"&3\"");
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
			sendMessage("RM20 8 \"" + "Er vægten ubelastet?" + "\" \"\" \"&3\"");

			boolean unloadConfirmed = false;
			while(!unloadConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();

				if(inputArr[1].equals("A")) {
					unloadConfirmed = true;
					sendMessage("T");
					
				} else {
					sendMessage("RM20 8 \"Fjern vægten og bekræft" + "U?" + "\" \"\" \"&3\"");
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
			pw.println(msg);
			pw.flush();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void taraProcedure() { //hej
		try { 
			System.out.println("Slet denne kommentar");
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String msg = "Placere tara.";
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
					System.out.println("tara successful");
					
				} else {
					msg = "Proev igen og godkend.";
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

	}
	
}