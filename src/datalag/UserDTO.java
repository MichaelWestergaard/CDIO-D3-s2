package datalag;



public class UserDTO {
	int BrugerId;                     
	String BrugerNavn;                    
	String Fornavn;
	String Efternavn;
	String CPR;                 
	String Password;
	Roller Rolle;
	boolean Aktiv;
	
	UserDTO(int BrugerID, String BrugerNavn, String Fornavn, String Efternavn, String CPR, String Password, Roller rolle, Boolean Aktiv){
		this.BrugerId = BrugerID;
		this.BrugerNavn = BrugerNavn;
		this.Fornavn = Fornavn;
		this.Efternavn = Efternavn;
		this.CPR = CPR;
		this.Password = Password;
		this.Rolle = Rolle;
		this.Aktiv = Aktiv;
	}
}
