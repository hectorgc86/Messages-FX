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

public class PostRegister extends Service<OkResponse> {
    private final User usu;

    public PostRegister(User usu) {
        this.usu = usu;
    }
    
    @Override
    protected Task<OkResponse> createTask() {
        return new Task<>() {
            @Override
            protected OkResponse call() {

                Gson gson = new Gson();
                String req =  gson.toJson(usu);
                String resp = ServiceUtils.getResponse(
                        NodeServer.getServer() + "/register",
                       req, "POST");

                Type type = new TypeToken<OkResponse>(){}.getType();

                return gson.fromJson(resp,type);
            }
        };
    }
}
