package hk.edu.hkbu.comp.simplelistview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private String[] pokemons = {
            "Bulbasaur",
            "Ivysaur",
            "Venusaur",
            "Charmander",
            "Charmeleon",
            "Charizard",
            "Squirtle",
            "Wartortle",
            "Blastoise",
            "Caterpie",
            "Metapod",
            "Butterfree"
    };

    private Integer[] pokemons_images = {
            R.drawable.pi001_bulbasaur, R.drawable.pi002_ivysaur,
            R.drawable.pi003_venusaur, R.drawable.pi004_charmander,
            R.drawable.pi005_charmeleon, R.drawable.pi006_charizard,
            R.drawable.pi007_squirtle, R.drawable.pi008_wartortle,
            R.drawable.pi009_blastoise, R.drawable.pi010_caterpie,
            R.drawable.pi011_metapod, R.drawable.pi012_butterfree
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        CustomListAdapter adapter = new CustomListAdapter(this, pokemons, pokemons_images);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ProfileActivity.class);

            intent.putExtra("name", pokemons[position]);
            intent.putExtra("image", pokemons_images[position]);
            startActivity(intent);
        });
    }


}
