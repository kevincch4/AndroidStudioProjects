package hk.edu.hkbu.comp.simplelistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kevin on 25/10/2017.
 */

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        Integer image = intent.getIntExtra("image", -1);

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(name);

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(image);
    }
}
