package models;

import javafx.scene.image.ImageView;
import services.NodeServer;

public class User {
    private String _id;
    private String name;
    private String password;
    private String image;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, String image) {
        this.name = name;
        this.password = password;
        this.image = image;
    }

    public User(String image) {

        this.image = image;
    }
    public void set_id(String _id) { this._id = _id; }

    public String get_id() { return _id;}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public ImageView getImageView() {
        ImageView imageView = new ImageView(NodeServer.getServer() + "/" + image);
        imageView.setFitHeight(30);
        imageView.setPreserveRatio(true);
        return imageView;
    }

}

