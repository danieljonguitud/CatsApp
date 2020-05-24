package com.effectivo.CatsApp;

import com.effectivo.CatsApp.persistence.models.Cat;
import com.effectivo.CatsApp.persistence.service.CatService;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int option_menu = -1;
        String[] buttons = {" 1. see cats", "2. see favorite", "3. exit", };

        do{
            //Principal Menu
            String option = (String) JOptionPane.showInputDialog(null, "Java Cats", "Principal Menu", JOptionPane.INFORMATION_MESSAGE,
                    null, buttons, buttons[0]);

            //Validate the user option
            for(int i=0;i<buttons.length;i++){
                if(option.equals(buttons[i])){
                    option_menu = i;
                }
            }

            switch(option_menu){
                case 0:
                    CatService.seeCats();
                    break;
                case 1:
                    Cat cat = new Cat();
                    CatService.seeFavorites(cat.getApiKey());
                default:
                    break;
            }
        }while(option_menu != 1);
    }
}
