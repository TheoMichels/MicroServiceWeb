package fr.polytech.authentification.authentifDemo.Controller.Exceptions;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException(Long id) {
        super("Wrong password for user n°"+id);
    }
}
