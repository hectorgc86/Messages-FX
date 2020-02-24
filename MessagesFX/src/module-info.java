module MessagesFX {

    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.google.gson;

    opens messagesfx;
    opens messagesfx.login;
    opens messagesfx.register;
    opens messagesfx.messages;
    opens models;
    opens services;
}