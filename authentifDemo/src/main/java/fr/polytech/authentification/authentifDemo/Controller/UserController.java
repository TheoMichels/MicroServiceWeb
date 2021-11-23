package fr.polytech.authentification.authentifDemo.Controller;

import fr.polytech.authentification.authentifDemo.Controller.Exceptions.NotValidTokenException;
import fr.polytech.authentification.authentifDemo.Controller.Exceptions.UserInUseException;
import fr.polytech.authentification.authentifDemo.Controller.Exceptions.UserNotFoundException;
import fr.polytech.authentification.authentifDemo.Controller.Exceptions.WrongPasswordException;
import fr.polytech.authentification.authentifDemo.Model.Token;
import fr.polytech.authentification.authentifDemo.Model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Token, User> usersToken = new HashMap<>();
    private final Map<String, Token> tokens = new HashMap<>();

    @PutMapping(value = "/AS/users")
    public User createUser(@RequestBody @Valid User user) {
        if (users.containsKey(user.getId())) {
            throw new UserInUseException(user.getId());
        } else {
            users.put(user.getId(), user);
        }
        return user;
    }

    @GetMapping(value = "AS/users/{userId}")
    public Long userExists(@PathVariable(value = "userId") Long id) {
        if (users.containsKey(id)) {
            return id;
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @DeleteMapping(value = "AS/users/{userId}")
    public void deleteUser(@PathVariable(value = "userId") Long id, @RequestHeader(value="X-token") String token_key) {
        if(tokenIsValid(getToken(token_key)) && usersToken.get(getToken(token_key)).getId().equals(id)) {
            if (users.containsKey(id)) {
                users.remove(id);
            } else {
                throw new UserNotFoundException(id);
            }
        } else {
            throw new NotValidTokenException();
        }
    }

    @PutMapping(value = "AS/users/{userId}/password")
    public String modifyPassword(@PathVariable(value = "userId") Long id, @RequestHeader(value="X-token") String token_key, @RequestBody @Valid String password) {

        if(tokenIsValid(getToken(token_key)) && usersToken.get(getToken(token_key)).getId().equals(id)) {
            if (users.containsKey(id)) {
                User user = users.get(id);
                user.setPassword(password);
                return password;
            } else {
                throw new UserNotFoundException(id);
            }
        } else {
            throw new NotValidTokenException();
        }
    }

    @PostMapping(value = "AS/users/{userId}/token")
    public String loginToken(@PathVariable(value = "userId") Long id, @RequestBody String password) {
        if (users.containsKey(id)) {
            if(usersToken.containsValue(users.get(id))) {
                for(Map.Entry<Token, User> p : usersToken.entrySet()) {
                    if(p.getValue().getId().equals(id)) {
                        usersToken.remove(p);
                    }
                }
            }
            if (password.equals(users.get(id).getPassword())) {
                Token token = new Token();
                boolean distinctKey = false;
                while(!distinctKey) {
                    if (!tokens.containsKey(token.getKey())) {
                        distinctKey = true;
                    }
                }
                tokens.put(token.getKey(), token);
                usersToken.put(token, users.get(id));
                return token.getKey();
            }
            else {
                throw new WrongPasswordException(id);
            }
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @DeleteMapping(value = "AS/users/{userId}/token")
    public void deleteToken(@PathVariable(value = "userId") Long id, @RequestHeader(value = "X-token") String token_key) {

        Token token = getToken(token_key);

        if(usersToken.containsKey(token) && usersToken.get(token).getId().equals(id)) {
            usersToken.remove(token);
            tokens.remove(token_key);
        }
        else {
            throw new NotValidTokenException();
        }
    }

    @GetMapping(value = "/AS/token")
    public Long tokenValid(@RequestHeader(value = "X-token") String token_key) {
        if (tokenIsValid(getToken(token_key))) {
            return usersToken.get(getToken(token_key)).getId();
        }
        else {
            throw new NotValidTokenException();
        }
    }

    public Boolean tokenIsValid(Token token) {
        return usersToken.containsKey(token) && !token.hasExpired() && users.containsValue(usersToken.get(token));
    }

    public Token getToken(String key) {
        if(tokens.containsKey(key)) {
            return tokens.get(key);
        }
        else {
            throw new NotValidTokenException();
        }
    }
}
