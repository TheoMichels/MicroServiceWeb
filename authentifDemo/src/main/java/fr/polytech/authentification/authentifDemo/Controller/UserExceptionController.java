package fr.polytech.authentification.authentifDemo.Controller;

import fr.polytech.authentification.authentifDemo.Controller.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionController {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundExceptionHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String userInUseExceptionHandler(UserInUseException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotValidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String notValidTokenExceptionHandler(NotValidTokenException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String wrongPasswordExceptionHandler(WrongPasswordException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WrongAssociatedTokenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String wrongAssociatedTokenExceptionHandler(WrongAssociatedTokenException ex) {
        return ex.getMessage();
    }
}
