package com.kevin.fyp;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        final ListView listView = (ListView) findViewById(R.id.listView);

        CustomListAdapter adapter = new CustomListAdapter(this, clothes, clothes_images);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CurrentClothes.currentClothes = clothes[i];
                Toast.makeText(getApplicationContext(), clothes[i], Toast.LENGTH_LONG).show();
                Log.d("CURRENT CLOTHES", CurrentClothes.currentClothes);
//                if(CurrentClothes.currentClothes.equals("polo.wt3")){
//                    Log.d("WORLD PATH","polo ");
//                }
            }
        });

    }



}
