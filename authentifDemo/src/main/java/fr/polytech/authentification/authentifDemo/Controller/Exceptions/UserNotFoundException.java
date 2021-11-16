package fr.polytech.authentification.authentifDemo.Controller.Exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User n°"+id+" not found");
    }
}
