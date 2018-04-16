package com.kevin.fyp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Kevin on 5/4/2018.
 */

public class Clothes {

    static ArrayList<String> name = new ArrayList<String>(Arrays.asList("purple shirt"));



    public static ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> clothes_name) {
        this.name = clothes_name;
    }

    public static int getSize(){
        for (int i = 0; i<name.size();i++){
            Log.d("ArrayList"+i, name.get(i));
        }
        return name.size();
    }


    private static Integer[] clothes_images = {
            R.drawable.purple, R.drawable.polo,
            R.drawable.men_shirt, R.drawable.women_shirt
    };

    public static Integer[] getPhoto(){
        return clothes_images;
    }



}
