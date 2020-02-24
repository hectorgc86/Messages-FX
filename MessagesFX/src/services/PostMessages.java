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
import models.Message;
import models.User;
import responses.OkResponse;

import java.lang.reflect.Type;

public class PostMessages extends Service<OkResponse> {
    private final Message mess;

    public PostMessages(Message mess) {
        this.mess = mess;
    }

    @Override
    protected Task<OkResponse> createTask() {
        return new Task<>() {
            @Override
            protected OkResponse call() {

                Gson gson = new Gson();
                String req =  gson.toJson(mess);
                String resp = ServiceUtils.getResponse(
                        NodeServer.getServer() + "/messages/"+mess.getTo(),
                        req, "POST");

                Type type = new TypeToken<OkResponse>(){}.getType();

                return gson.fromJson(resp,type);
            }
        };
    }
}

