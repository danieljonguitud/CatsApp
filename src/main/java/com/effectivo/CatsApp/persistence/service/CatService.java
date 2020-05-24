package com.effectivo.CatsApp.persistence.service;


import com.effectivo.CatsApp.persistence.models.Cat;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {

    public static void seeCats() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").method("get").build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        // Cutting the brackets
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length()-1);

        // Creating Gson class object
        Gson gson = new Gson();
        Cat cat = gson.fromJson(elJson, Cat.class);

        // Redimensionar en caso de necesitar
        Image image = null;

        try{
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);

            ImageIcon backgroundCat = new ImageIcon(image);

            if(backgroundCat.getIconWidth() > 800){
                Image background = backgroundCat.getImage();
                Image modification = background.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                backgroundCat = new ImageIcon(modification);
            }

            String menu = "Opciones: \n"
                    + "1. See another one \n"
                    + "2. favorite \n"
                    + "3. back \n";


        } catch(IOException e) {
            System.out.println(e);

        }
    }

}
