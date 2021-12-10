package fr.polytech.microservices.MicroServices.Controller.Exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(Long user_id, String token) { super("Token" + token + "not valid for user "+ user_id);}
}
