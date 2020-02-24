package messagesfx.register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;
import responses.OkResponse;
import services.PostRegister;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;

import static utils.FileChooser.loadImageFile;
import static utils.ScreenLoader.loadScreen;

public class RegisterController {

    @FXML
    private ImageView photoImageView;

    @FXML
    private TextField secondPassTextView;

    @FXML
    private TextField userTextView;

    @FXML
    private TextField passwordTextView;

    @FXML
    private Label errorLabel;

    private void goToLogin(ActionEvent event){

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            loadScreen("/messagesfx/login/Login.fxml",stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  File photo;
    @FXML
    void cancelButtonAction(ActionEvent event){
        goToLogin(event);
    }

    @FXML
    void registerButtonAction(ActionEvent event) {

        if(userTextView.getText().isEmpty() || passwordTextView.getText().isEmpty() ||
                secondPassTextView.getText().isEmpty()){
                errorLabel.setText("One or more fields are empty");
        }else if (!passwordTextView.getText().equals(secondPassTextView.getText())){
                errorLabel.setText("Password fields doesn't match");
        }else if (photoImageView == null){
                errorLabel.setText("Select an image before register");
        }else{
            String name = userTextView.getText();
            String password = passwordTextView.getText();
            String img ="";
            try {
                byte[] bytes = Files.readAllBytes(photo.toPath());
                 img = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException ex) {
                System.err.println("Error getting bytes from " + photo.toString());
            }

            User userToRegister = new User(name, password,img);

            PostRegister postRegister = new PostRegister(userToRegister);
            postRegister.start();

            postRegister.setOnSucceeded(e -> {
                OkResponse resp = postRegister.getValue();

               if(resp.getOk()){

                   Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                   dialog.setTitle("Register complete");
                   dialog.setHeaderText("You have successfully registered");
                   dialog.setContentText("You will be redirected to the login page");
                   Optional<ButtonType> result = dialog.showAndWait();

                   if (result.get() == ButtonType.OK){
                       goToLogin(event);
                   }
               }else{
                   errorLabel.setText("Error: "+ resp.getError());
               }
            });
        }
    }

    @FXML
    void selectImageButtonAction(ActionEvent event) {
        photo = loadImageFile(photoImageView);

    }

}
