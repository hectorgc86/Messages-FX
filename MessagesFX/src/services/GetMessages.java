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
import responses.MessagesResponse;

import java.lang.reflect.Type;

public class GetMessages extends Service<MessagesResponse> {

    @Override
    protected Task<MessagesResponse> createTask() {
        return new Task<>() {
            @Override
            protected MessagesResponse call() {

                Gson gson = new Gson();
                String resp = ServiceUtils.getResponse(
                        NodeServer.getServer() + "/messages",
                        null, "GET");

                Type type = new TypeToken<MessagesResponse>() {
                }.getType();

                return gson.fromJson(resp, type);
            }
        };
    }
}

