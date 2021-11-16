package fr.polytech.authentification.authentifDemo.Controller.Exceptions;

public class NotValidTokenException extends RuntimeException {

    public NotValidTokenException() {
        super("Not valid token");
    }
}
