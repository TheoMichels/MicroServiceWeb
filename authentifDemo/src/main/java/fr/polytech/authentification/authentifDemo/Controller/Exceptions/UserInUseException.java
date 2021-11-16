package fr.polytech.authentification.authentifDemo.Controller.Exceptions;

public class UserInUseException extends RuntimeException{

    public UserInUseException(Long id) {
        super("User N°"+id+" already used");
    }
}
