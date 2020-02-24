package responses;

import models.User;

import java.util.List;

public class UsersResponse {

    private Boolean ok;
    private List<User> users;
    private String error;

    public UsersResponse(Boolean ok, List<User> users, String error) {
        this.ok = ok;
        this.users = users;
        this.error = error;
    }

    public Boolean getOk() { return ok; }

    public void setOk(Boolean ok) { this.ok = ok;}

    public List<User> getUsers() { return users; }

    public void setUsers(List<User> users) { this.users = users; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }
}
