package models;

import javafx.scene.image.ImageView;
import services.NodeServer;

public class Message {

    private String _id;
    private User from;
    private String to;
    private String message;
    private String image;
    private String sent;

    public Message(User from, String to, String message, String image) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = image;
    }

    public void set_id(String _id) { this._id = _id; }

    public String get_id() { return _id; }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) { this.from = from; }

    public String getTo() {
        return to;
    }

    public void setTo(String to) { this.to = to; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getSent() { return sent; }

    public void setSent(String sent) { this.sent = sent; }

    public ImageView getImageView() {
        ImageView imageView = new ImageView(NodeServer.getServer() + "/" + image);
        imageView.setFitHeight(30);
        imageView.setPreserveRatio(true);
        return imageView;
    }

}
