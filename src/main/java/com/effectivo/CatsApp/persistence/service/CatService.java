package com.effectivo.CatsApp.persistence.service;


import com.effectivo.CatsApp.persistence.models.Cat;
import com.effectivo.CatsApp.persistence.models.CatFav;
import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {

    public static void seeCats() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").method("GET", null).build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        // Cutting the brackets
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length() - 1);

        // Creating Gson class object
        Gson gson = new Gson();
        Cat cat = gson.fromJson(elJson, Cat.class);

        // Redimensionar en caso de necesitar
        Image image = null;

        try {
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);

            ImageIcon backgroundCat = new ImageIcon(image);

            if (backgroundCat.getIconWidth() > 800) {
                Image background = backgroundCat.getImage();
                Image modification = background.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                backgroundCat = new ImageIcon(modification);
            }

            String menu = "Options: \n"
                    + "1. see another one \n"
                    + "2. favorite \n"
                    + "3. back \n";

            String[] buttons = {"see another one", "favorite", "back"};
            String id_cat = cat.getId();
            String option = (String) JOptionPane.showInputDialog(null,
                    menu,
                    id_cat,
                    JOptionPane.INFORMATION_MESSAGE,
                    backgroundCat,
                    buttons,
                    buttons[0]);

            int selection = -1;

            //Validate the user option
            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    selection = i;
                }
            }

            switch (selection) {
                case 0:
                    seeCats();
                    break;
                case 1:
                    favoriteCat(cat);
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            System.out.println(e);

        }
    }

    public static void favoriteCat(Cat cat) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json,text/plain");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"image_id\": \"" + cat.getId() + "\"\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cat.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void seeFavorites(String apiKey) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build();
        Response response = client.newCall(request).execute();

        // guardamos el string con la respuesta
        String elJson = response.body().string();

        //creamos el objeto gson
        Gson gson = new Gson();

        CatFav[] catsArray = gson.fromJson(elJson, CatFav[].class);

        if (catsArray.length > 0) {
            int min = 1;
            int max = catsArray.length;
            int random = (int) (Math.random() * ((max - min) - 1)) + min;
            int index = random - 1;

            CatFav catFav = catsArray[index];

            Image image = null;

            try {
                URL url = new URL(catFav.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon backgroundCat = new ImageIcon(image);

                if (backgroundCat.getIconWidth() > 800) {
                    //redimensionamos
                    Image backgraound = backgroundCat.getImage();
                    Image modified = backgraound.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    backgroundCat = new ImageIcon(modified);
                }

                String menu = "Options: \n"
                        + " 1. see another one \n"
                        + " 2. delete favorite \n"
                        + " 3. back \n";

                String[] buttons = {"see another one", "delete favorite", "back"};
                String id_cat = catFav.getId();
                String option = (String) JOptionPane.showInputDialog(null, menu, id_cat, JOptionPane.INFORMATION_MESSAGE, backgroundCat, buttons, buttons[0]);

                int selection = -1;
                //validamos que opcion selecciona el usuario
                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])) {
                        selection = i;
                    }
                }

                switch (selection) {
                    case 0:
                        seeFavorites(apiKey);
                        break;
                    case 1:
                        deleteFavorite(catFav);
                        break;
                    default:
                        break;
                }
            } catch (IOException e){
                System.out.println(e);
            }
        }
    }

    private static void deleteFavorite(CatFav catfav) {
    }
}