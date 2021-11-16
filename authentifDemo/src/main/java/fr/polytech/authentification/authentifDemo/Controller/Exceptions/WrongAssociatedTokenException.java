package fr.polytech.authentification.authentifDemo.Controller.Exceptions;

public class WrongAssociatedTokenException extends RuntimeException {

    public WrongAssociatedTokenException(Long id) {
        super("This token is associated to an other user than user n°"+id);
    }
}
