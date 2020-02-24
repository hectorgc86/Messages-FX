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
import responses.OkResponse;

import java.lang.reflect.Type;

public class PutUsers extends Service<OkResponse> {

    private final User user;

    public PutUsers(User user) {
        this.user = user;
    }

    @Override
    protected Task<OkResponse> createTask() {
        return new Task<>() {
            @Override
            protected OkResponse call() {
                Gson gson = new Gson();
                String resp = ServiceUtils.getResponse(
                        "http://localhost:8080/users", gson.toJson(user), "PUT");

                Type type = new TypeToken<OkResponse>() {
                }.getType();

                return gson.fromJson(resp, type);
            }
        };
    }
}


