package responses;

public class LoginResponse {

    private Boolean ok;
    private String error;
    private String name;
    private String token;
    private String image;

    public LoginResponse(Boolean ok, String error,String name, String token, String image) {
        this.ok = ok;
        this.error = error;
        this.name = name;
        this.image = image;
        this.token = token;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name;}

    public String getToken() { return token;}

    public void setToken(String token) { this.token = token;}

    public String getImage() { return image;}

    public void setImage(String image) { this.image = image;}
}
