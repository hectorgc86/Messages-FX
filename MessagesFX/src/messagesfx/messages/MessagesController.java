package messagesfx.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import models.Message;
import models.User;
import responses.MessagesResponse;
import responses.OkResponse;
import responses.UsersResponse;
import services.*;
import utils.MessageUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static messagesfx.login.LoginController.loggedUser;
import static utils.FileChooser.loadImageFile;

public class MessagesController {

    @FXML
    private TableView<Message> userMessagesTableView;

    @FXML
    private TableView<User> usersRegisteredTableView;

    @FXML
    private TableColumn<Message, ImageView> imageColumn;

    @FXML
    private TableColumn<User, ImageView> avatarColumn;

    @FXML
    private TableColumn<Message, String> messageColumn;

    @FXML
    private TableColumn<Message, LocalDateTime> sentColumn;

    @FXML
    private TableColumn<User, String> nickNameColumn;

    @FXML
    private Button deleteMessageButton;

    @FXML
    private ImageView selectedImageView;

    @FXML
    private Button sendMessageButton;

    @FXML
    private Label userNameLabel;

    @FXML
    private ImageView userImageView;

    @FXML
    private TextField messageTextField;

    private File photo;
    public void initialize(){

        messageColumn.setCellValueFactory(new PropertyValueFactory("message"));
        imageColumn.setCellValueFactory(new PropertyValueFactory("imageView"));
        sentColumn.setCellValueFactory(new PropertyValueFactory("sent"));
        avatarColumn.setCellValueFactory(new PropertyValueFactory("imageView"));
        nickNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        userNameLabel.setText(loggedUser.getName());
        userImageView.setImage(new Image(NodeServer.getServer() + "/" + loggedUser.getImage()));

        bringUserMessages();
        bringUsers();
    }

    private void bringUsers() {
        GetUsers getUsers = new GetUsers();
        getUsers.start();

        getUsers.setOnSucceeded(e -> {
            UsersResponse resp = getUsers.getValue();

            if(resp.getOk()){
                if(resp.getUsers().size() > 0){
                    updateUsersList(resp.getUsers());
                }
            }
        });
    }

    private void bringUserMessages() {

        GetMessages getMess = new GetMessages();
        getMess.start();

        getMess.setOnSucceeded(e -> {
            MessagesResponse resp = getMess.getValue();

            if(resp.getOk()){
                List<Message> userMessages = resp.getMessages().stream().filter(m->m.getFrom().getName().equals
                        (loggedUser.getName())).collect(Collectors.toList());
                if(userMessages.size() > 0){
                    updateUserMessagesList(userMessages);
                }
            }
        });
    }

    private void updateUserMessagesList(List<Message> messagesList) {

        ObservableList<Message> messages = FXCollections.observableArrayList(messagesList);
        userMessagesTableView.setItems(messages);
    }

    private void updateUsersList(List<User> usersList) {
        ObservableList<User> users = FXCollections.observableArrayList(usersList);
        usersRegisteredTableView.setItems(users);
    }

    @FXML
    void sendMessageButtonAction(ActionEvent event) {
        if(selectedImageView.getImage() != null && !messageTextField.getText().isEmpty() &&
                usersRegisteredTableView.getSelectionModel().getSelectedItem() != null){

            User from = loggedUser;
            String to = usersRegisteredTableView.getSelectionModel().getSelectedItem().get_id();
            String message = messageTextField.getText();
            String image = "";
            try {
                byte[] bytes = Files.readAllBytes(photo.toPath());
                image = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException ex) {
                System.err.println("Error getting bytes from " + photo.toString());
            }
            Message mess = new Message(from,to,message,image);
            PostMessages postMes = new PostMessages(mess);

            postMes.start();

            postMes.setOnSucceeded(e -> {
                OkResponse resp = postMes.getValue();

                if(resp.getOk()){
                    MessageUtils.showMessage("Message sent","The message was succesfully sent to "+
                            usersRegisteredTableView.getSelectionModel().getSelectedItem().getName());
                }else{
                    MessageUtils.showError("Error",resp.getError());
                }
            });
        }else{
            MessageUtils.showError("Error","Select image and user to send message");
        }
    }

    @FXML
    void deleteMessageButtonAction(ActionEvent event) {

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setHeaderText("Really wants to delete selected message?");
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ButtonType.OK){
            DelMessages delMess = new DelMessages(userMessagesTableView.getSelectionModel().getSelectedItem());
            delMess.start();

            delMess.setOnSucceeded(e -> {
                OkResponse resp = delMess.getValue();
                if(!resp.getOk()){
                    MessageUtils.showError("Error",resp.getError());
                }
            });
        }
        deleteMessageButton.setDisable(true);
    }

    @FXML
    void changeImageButtonAction(ActionEvent event) {
        photo = loadImageFile(userImageView);

        String image = "";
        try {
            byte[] bytes = Files.readAllBytes(photo.toPath());
            image = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            System.err.println("Error getting bytes from " + photo.toString());
        }
      User us = new User(image);

        PutUsers putUs = new PutUsers(us);

        putUs.start();

        putUs.setOnSucceeded(e -> {
            OkResponse resp = putUs.getValue();

            if(resp.getOk()){
                MessageUtils.showMessage("Image change","The image was succesfully changed ");
            }else{
                MessageUtils.showError("Error",resp.getError());
            }
        });

    }

    @FXML
    void refreshButtonAction(ActionEvent event) {
        bringUserMessages();
    }

    @FXML
    void selectImageButtonAction(ActionEvent event) {
        photo = loadImageFile(selectedImageView);
    }

    @FXML
    void userMessagesOnClick(MouseEvent event) {
        if(userMessagesTableView.getSelectionModel().getSelectedItem() != null){
            deleteMessageButton.setDisable(false);
        }else{
            deleteMessageButton.setDisable(true);
        }
    }

    @FXML
    void messageOnKeyReleased(KeyEvent event) {
        if(messageTextField.getText().length() == 0){
            sendMessageButton.setDisable(true);
        }else{
            sendMessageButton.setDisable(false);
        }
    }
}
