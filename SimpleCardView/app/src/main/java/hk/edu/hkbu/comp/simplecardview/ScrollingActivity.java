package hk.edu.hkbu.comp.simplecardview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private List<Pokemon> pokemons;

    private void initializeData(){
        pokemons = new ArrayList<>();
        pokemons.add(new Pokemon("001", "Bulbasaurd", R.drawable.pi001_bulbasaur));
        pokemons.add(new Pokemon("002", "Ivysaur", R.drawable.pi002_ivysaur));
        pokemons.add(new Pokemon("003", "Venusaur", R.drawable.pi003_venusaur));
        pokemons.add(new Pokemon("004", "Charmander", R.drawable.pi004_charmander));
        pokemons.add(new Pokemon("005", "Charmeleon", R.drawable.pi005_charmeleon));
        pokemons.add(new Pokemon("006", "Charizard", R.drawable.pi006_charizard));
        pokemons.add(new Pokemon("007", "Squirtle", R.drawable.pi007_squirtle));
        pokemons.add(new Pokemon("008", "Wartortle", R.drawable.pi008_wartortle));
        pokemons.add(new Pokemon("009", "Blastoise", R.drawable.pi009_blastoise));
        pokemons.add(new Pokemon("010", "Caterpie", R.drawable.pi010_caterpie));
        pokemons.add(new Pokemon("011", "Metapod", R.drawable.pi011_metapod));
        pokemons.add(new Pokemon("012", "Butterfree", R.drawable.pi012_butterfree));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
