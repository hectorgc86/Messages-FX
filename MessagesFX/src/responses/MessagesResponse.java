package responses;

import models.Message;

import java.util.List;

public class MessagesResponse {

    private Boolean ok;
    private List<Message> messages;
    private String error;

    public MessagesResponse(Boolean ok, List<Message> messages, String error) {
        this.ok = ok;
        this.messages = messages;
        this.error = error;
    }

    public Boolean getOk() { return ok; }

    public void setOk(Boolean ok) { this.ok = ok;}

    public List<Message> getMessages() { return messages; }

    public void setMessages(List<Message> messages) { this.messages = messages; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }
}
