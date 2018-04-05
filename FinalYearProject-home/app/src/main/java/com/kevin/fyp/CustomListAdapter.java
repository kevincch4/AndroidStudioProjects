package com.kevin.fyp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kevin on 5/4/2018.
 */

public class CustomListAdapter extends ArrayAdapter {
    private final Context context;
    private final String[] clothes;
    private final Integer[] images;

    public CustomListAdapter(@NonNull Context context, @NonNull String[] clothes,@NonNull Integer[] images) {
        super(context, R.layout.activity_gallery, clothes);
        this.context = context;
        this.clothes = clothes;
        this.images = images;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(clothes[position]);
        imageView.setImageResource(images[position]);

        return rowView;

    };
}
