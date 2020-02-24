package responses;

public class OkResponse {

    private Boolean ok;
    private String error;

    public OkResponse(Boolean ok, String error) {
        this.ok = ok;
        this.error = error;
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
}
