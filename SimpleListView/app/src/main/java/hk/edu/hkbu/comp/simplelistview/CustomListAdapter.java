package hk.edu.hkbu.comp.simplelistview;

/**
 * Created by Kevin on 25/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] pokemons;
    private final Integer[] images;

    public CustomListAdapter(@NonNull Context context, @NonNull String[] pokemons, @NonNull Integer[] images) {
        super(context, R.layout.list_item, pokemons);

        this.context = context;
        this.pokemons = pokemons;
        this.images = images;
    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(pokemons[position]);
        imageView.setImageResource(images[position]);

        return rowView;

    };
}
