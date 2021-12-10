package fr.polytech.microservices.MicroServices.Controller.Exceptions;

public class EmailInUseException extends RuntimeException {
	
	public EmailInUseException(String email) {
		super("Email in use: " + email);
	}

}
