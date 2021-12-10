package fr.polytech.microservices.MicroServices.Model;

public class AuthServiceUser {

    private Long id;
    private String password;

    public AuthServiceUser(Long id) {
        super();
        this.id = id;
        this.password = "default_password";
    }

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
