package fr.polytech.authentification.authentifDemo.Model;

public class User {

    Long id;
    String password = "default_password";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
