package controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import DAO.UserDAO;
import DTO.UserDTO;
import datalag.ResponseHandler;

public class UserController extends ResponseHandler {
	
	private UserDAO userDAO = new UserDAO();
	
	public String login(String username) {
		try {
			List<UserDTO> users = userDAO.list();
			
			for (UserDTO user : users) {
				if(user.getUserName().equalsIgnoreCase(username)) {
					if(user.getActive() == 1) {
						return createResponse("success", 1, String.valueOf(user.getUserID()));
					} else {
						return createResponse("error", 0, "Brugeren er inaktiv!");
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke logge ind");
	}
	
	public String createUser(int userID, String userName, String firstName, String lastName, String CPR, String password, String initial, List<String> role, int active) {
		try {
			List<UserDTO> users = userDAO.list();

			if(userID >= 1 && userID <= 999) {
				if(userName.length() >= 2 && userName.length() <= 20) {
					if(role.size() > 0) {
						if(CPR.length() == 11) {
							String[] splitCPR = CPR.split("-");
							String combinedCPR = splitCPR[0] + splitCPR[1];
							char[] cprChars = combinedCPR.toCharArray();
							int cprDay = Integer.parseInt(new StringBuilder().append(cprChars[0]).append(cprChars[1]).toString());
							int cprMonth = Integer.parseInt(new StringBuilder().append(cprChars[2]).append(cprChars[3]).toString());
							int cprYear = Integer.parseInt(new StringBuilder().append(cprChars[4]).append(cprChars[5]).toString());
	
							if(cprDay > 0 && cprDay < 32 && cprMonth > 0 && cprMonth < 13 && cprYear >= 0 && cprYear <= 99) {
								for (UserDTO user : users) {
									if(user.getCpr().equals(CPR)) {
										return createResponse("error", 0, "CPR-nummeret findes allerede!");
									}
								}
							} else {
								return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
							}
						} else {
							return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
						}
					} else {
						return createResponse("error", 0, "Du skal minimum have en rolle");
					}
					
					if(initial.length() >= 2 && initial.length() <= 4) {
						for (UserDTO user : users) {
							if(user.getInitial().equals(initial)) {
								return createResponse("error", 0, "Initialerne findes allerede!");
							}
						}
					} else {
						return createResponse("error", 0, "Initialer skal være 2-4 tegn!");
					}
				} else {
					return createResponse("error", 0, "Brugernavnet skal være 2-20 tegn!");
				}
			} else {
				return createResponse("error", 0, "Bruger ID skal være i mellem 1-999!");
			}
			
			if(userDAO.read(userID) == null) {
				if(userDAO.create(new UserDTO(userID, userName, firstName, lastName, CPR, password, initial, role, active))) {					
					return createResponse("success", 1, "Brugeren \"" + userName + "\" blev oprettet");
				}
			} else {
				return createResponse("error", 0, "Bruger ID'et er taget");
			}
			
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke oprette brugeren");
	}
	
	public String updateUser(int userID, String userName, String firstName, String lastName, String CPR, String password, String initial, String role, int active) {
		try {
			List<UserDTO> users = userDAO.list();
			
			if(userName.length() >= 2 && userName.length() <= 20) {
				if(!role.isEmpty()) {
					if(CPR.length() == 11) {
						String[] splitCPR = CPR.split("-");
						String combinedCPR = splitCPR[0] + splitCPR[1];
						char[] cprChars = combinedCPR.toCharArray();
						int cprDay = Integer.parseInt(new StringBuilder().append(cprChars[0]).append(cprChars[1]).toString());
						int cprMonth = Integer.parseInt(new StringBuilder().append(cprChars[2]).append(cprChars[3]).toString());
						int cprYear = Integer.parseInt(new StringBuilder().append(cprChars[4]).append(cprChars[5]).toString());
						
						if(cprDay > 0 && cprDay < 32 && cprMonth > 0 && cprMonth < 13 && cprYear >= 0 && cprYear <= 99) {
							for (UserDTO user : users) {
								if(user.getCpr().equals(CPR) && user.getUserID() != userID) {
									return createResponse("error", 0, "CPR-nummeret findes allerede!");
								}
							}
						} else {
							return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
						}
					} else {
						return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
					}
				} else {
					return createResponse("error", 0, "Du skal minimum have en rolle");
				}
				if(initial.length() >= 2 && initial.length() <= 4) {
					for (UserDTO user : users) {
						if(user.getInitial().equals(initial) && user.getUserID() != userID) {
							return createResponse("error", 0, "Initialerne findes allerede!");
						}
					}
				} else {
					return createResponse("error", 0, "Initialer skal være 2-4 tegn!");
				}
			} else {
				return createResponse("error", 0, "Brugernavnet skal være 2-20 tegn!");
			}
			
			UserDTO user = new UserDTO(userID, userName, firstName, lastName, CPR, password, initial, Arrays.asList(role.split(",")), active);
		
			if(userDAO.update(user)) {
				return createResponse("success", 1, "Brugeren blev opdateret");
			} else {
				return createResponse("error", 1, "Brugeren blev ikke opdateret");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
		
	}
	
	public String getUser(int userID) {
		try {
			return createResponse("success", 1, new Gson().toJson(userDAO.read(userID)));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}
	
	public String getUsers() {
		try {
			return createResponse("success", 1, new Gson().toJson(userDAO.list()));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
	}
	
	public String changeStatus(int userID) {
		try {
			UserDTO user = userDAO.read(userID);
			if(user != null) {
				
				int newStatus = ((user.getActive() == 0) ? 1 : 0);
				
				user.setActive(newStatus);
				
				if(userDAO.changeStatus(user)) {
					String msg = "";
					if(newStatus == 0) {
						msg = "Brugeren er hermed inaktiveret!";
					} else {
						msg = "Brugeren blev aktiveret igen!";
					}
					return createResponse("success", 1, msg);
				}
			} else {
				return createResponse("error", 0, "Brugeren findes ikke");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke ændre statussen");
	}
	
	public String resetPassword(int userID, String password) {
		try {
			UserDTO user = userDAO.read(userID);
			if(user != null) {
				user.setPassword(password);
				if(userDAO.resetPassword(user)) {
					return createResponse("success", 1, "Adgangskoden blev opdateret");
				} else {
					return createResponse("error", 1, "Adgangskoden blev ikke opdateret");
				}
			} else {
				return createResponse("error", 0, "Brugeren findes ikke");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
	}
	
}