package project_management.Project_Management.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import project_management.Project_Management.model.User;

@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private  String jwt;
    private String message;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

}
