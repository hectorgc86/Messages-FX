/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import models.User;
import responses.LoginResponse;

import java.lang.reflect.Type;

public class PostLogin extends Service<LoginResponse> {
    private final User user;

    public PostLogin(User user) {
        this.user = user;
    }

    @Override
    protected Task<LoginResponse> createTask() {
        return new Task<>() {
            @Override
            protected LoginResponse call() {

                Gson gson = new Gson();
                String resp = ServiceUtils.getResponse(
                        NodeServer.getServer() + "/login",
                        gson.toJson(user), "POST");

                Type type = new TypeToken<LoginResponse>() {
                }.getType();

                return gson.fromJson(resp, type);
            }
        };
    }
}

