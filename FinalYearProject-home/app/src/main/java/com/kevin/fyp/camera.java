package com.kevin.fyp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.internal.ShareDialogFeature;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;

public class camera extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CallbackManager callbackManager = CallbackManager.Factory.create();
    ShareDialog shareDialog = new ShareDialog(this);

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();

            if(ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto).build();
                shareDialog.show(content);
                Log.d("BITMAP LOADED","OKOKOKOKOK");
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //callbackManager = CallbackManager.Factory.create();
        //shareDialog = new ShareDialog(this);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((AppCompatActivity) this).getSupportActionBar().setTitle("");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //get name and icon from user and display it to the nav
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView displayName = headerView.findViewById(R.id.displayName);
        String name = getIntent().getStringExtra("name");
        Log.d("THIS IS NAME:  ", name);
        displayName.setText(name);
        String checkFbLogin = getIntent().getStringExtra("checkFbLogin");
        if (checkFbLogin.equals("Yes")) {
            String iconUrl = getIntent().getStringExtra("iconUrl");
            Log.d("THIS IS ICONURL:  ", iconUrl);
            new camera.DownloadImage((ImageView) headerView.findViewById(R.id.icon)).execute(iconUrl);
        } else if (checkFbLogin.equals("No")) {
            ImageView icon = headerView.findViewById(R.id.icon);
            icon.setImageResource(R.drawable.guest);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
            final Intent arIntent = new Intent(this, SampleCamActivity.class);
            startActivity(arIntent);
        } else if (id == R.id.nav_gallery) {
            //transfer to the activity_gallery page
            Intent gallery = new Intent (this, com.kevin.fyp.gallery.class);
            startActivity(gallery);
        } else if (id == R.id.nav_addModel) {
            //transfer to the download page
            Intent download = new Intent(this, com.kevin.fyp.download.class);
            startActivity(download);
        } else if (id == R.id.nav_share) {
            String checkFbLogin = getIntent().getStringExtra("checkFbLogin");
            if (checkFbLogin.equals("Yes")) {
                Toast.makeText(this, "Sharing Function to be implemented", Toast.LENGTH_SHORT).show();
            } else if (checkFbLogin.equals("No")) {
                Toast.makeText(this, "You have to login to share", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.logout) {
            LoginManager.getInstance().logOut();
            this.finish();
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //a class to download the user icon from a url
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    //card click
    public void onCardClick(View v){
        if(v.getId() == R.id.camera_card){
            // Handle the camera action
            Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
            final Intent arIntent = new Intent(this, SampleCamActivity.class);
            startActivity(arIntent);
        } else if(v.getId() == R.id.wardrobe_card){
            //transfer to the activity_gallery page
            Intent gallery = new Intent (this, com.kevin.fyp.gallery.class);
            startActivity(gallery);
        } else if(v.getId() == R.id.download_card){
            //transfer to the download page
            Intent download = new Intent(this, com.kevin.fyp.download.class);
            startActivity(download);
        } else if(v.getId() == R.id.logout_card){
//            LoginManager.getInstance().logOut();
//            this.finish();
//            Intent main = new Intent(this, MainActivity.class);
//            startActivity(main);

            Log.d("PRESSED CARD","OKOKOKOKOK");

            //create callback
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Log.d("SHARED","OKOKOKOKOK");
                    Toast.makeText(camera.this, "Share Successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(camera.this, "Share Cancel", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(camera.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

            //load photo to bitmap
            Picasso.with(this).load("https://koreaboo-cdn.storage.googleapis.com/2017/04/Irene-1.jpg")
                    .into(target);
            Log.d("PHOTO LOADED","OKOKOKOKOK");


        }
    }
}
