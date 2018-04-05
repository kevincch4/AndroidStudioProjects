package com.kevin.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by Kevin on 5/4/2018.
 */

public class gallery extends AppCompatActivity{
    private String[] clothes = new String[Clothes.getSize()];


    private Integer[] clothes_images = Clothes.getPhoto();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        clothes =  Clothes.getName().toArray(clothes);

//        String arraysize = String.valueOf(clothes.length);
//        String arralistsize = String.valueOf(Clothes.getSize());
//        Log.d("SIZE", arraysize);
//        Log.d("LISTSIZE", arralistsize);

        ListView listView = (ListView) findViewById(R.id.listView);

        CustomListAdapter adapter = new CustomListAdapter(this, clothes, clothes_images);


        listView.setAdapter(adapter);
    }
}
