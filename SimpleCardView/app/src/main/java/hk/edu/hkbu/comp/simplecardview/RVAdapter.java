package hk.edu.hkbu.comp.simplecardview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Kevin on 25/10/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PokemonViewHolder> {

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView pokedex;
        ImageView image;

        PokemonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            name = (TextView) itemView.findViewById(R.id.name);
            pokedex = (TextView) itemView.findViewById(R.id.pokedex);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}