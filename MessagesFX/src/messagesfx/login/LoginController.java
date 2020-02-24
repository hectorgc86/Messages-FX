package messagesfx.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import responses.LoginResponse;
import services.PostLogin;
import services.ServiceUtils;

import java.io.IOException;

import static utils.ScreenLoader.loadScreen;

public class LoginController {

    @FXML
    private TextField userTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label loginErrorLabel;


    public static User loggedUser;

    @FXML
    void loginButtonAction(ActionEvent event) {

        if (userTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            loginErrorLabel.setText("One or more fields are empty");
        } else {
            User auxUser = new User(userTextField.getText(), passwordTextField.getText());

            PostLogin postLogin = new PostLogin(auxUser);
            postLogin.start();

            postLogin.setOnSucceeded(e -> {

                LoginResponse loginResp = postLogin.getValue();
                if (loginResp.getOk()) {

                    loggedUser = new User(loginResp.getName(), passwordTextField.getText(), loginResp.getImage());

                    ServiceUtils.setToken(loginResp.getToken());

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    try {
                        loadScreen("/messagesfx/messages/Messages.fxml", stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    loginErrorLabel.setText(loginResp.getError());
                }
            });
        }
    }

    @FXML
    void createAccountAction(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            loadScreen("/messagesfx/register/Register.fxml", stage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
