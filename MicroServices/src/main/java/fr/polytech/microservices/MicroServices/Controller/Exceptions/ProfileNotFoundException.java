package fr.polytech.microservices.MicroServices.Controller.Exceptions;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(long id) {
        super("Profile not found"+ id);
    }
}
