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
import responses.UsersResponse;

import java.lang.reflect.Type;

public class GetUsers extends Service<UsersResponse> {

    @Override
    protected Task<UsersResponse> createTask() {
        return new Task<>() {
            @Override
            protected UsersResponse call() {

                Gson gson = new Gson();
                String resp = ServiceUtils.getResponse(
                        NodeServer.getServer() + "/users",
                        null, "GET");

                Type type = new TypeToken<UsersResponse>() {
                }.getType();

                return gson.fromJson(resp, type);
            }
        };
    }
}

