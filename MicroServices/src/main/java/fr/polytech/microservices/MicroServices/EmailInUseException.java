package fr.polytech.microservices.MicroServices;

public class EmailInUseException extends RuntimeException {
	
	public EmailInUseException(String email) {
		super("Email in use: " + email);
	}

}
