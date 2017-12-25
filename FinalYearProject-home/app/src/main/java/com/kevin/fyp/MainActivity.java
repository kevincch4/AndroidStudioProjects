package com.kevin.fyp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import com.kevin.fyp.R;

public class MainActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile","user_friends");

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
              goToNextPage(currentProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
               goToNextPage(profile);
                Toast.makeText(getApplicationContext(),"Logging in through FB", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                String userid = loginResult.getAccessToken().getUserId();
//                Log.d( "hi", userid);
//
//                goToNextPage();
//
////                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
////                    @Override
////                    public void onCompleted(JSONObject object, GraphResponse response) {
////                        displayUserInfo(object);
////                    }
////                });
////
////                Bundle parameters = new Bundle();
////                parameters.putString("fields", "first_name, last_name, email, id");
////                graphRequest.setParameters(parameters);
////                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        goToNextPage(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void displayUserInfo(JSONObject object){
        String first_name, last_name, email, id;
        first_name = "";
        last_name="";
        email = "";
        id = "";
        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView view_name, view_email,view_id;
        view_name = (TextView)findViewById(R.id.view_name);
        view_email = (TextView)findViewById(R.id.view_email);
        view_id = (TextView)findViewById(R.id.view_id);

        view_name.setText(first_name+" "+ last_name);
        view_email.setText(email);
        view_id.setText("ID: "+id);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goToNextPage(Profile profile) {
        if(profile != null) {
            Intent i = new Intent(this, camera.class);
            i.putExtra("name", profile.getName());
            i.putExtra("checkFbLogin", "Yes");
            i.putExtra("iconUrl", profile.getProfilePictureUri(200,200).toString());
            startActivity(i);
        }
    }

    public void onButtonClick(View v){
        if(v.getId() == R.id.skipButton){
            Intent i = new Intent(this, camera.class);
            i.putExtra("checkFbLogin", "No");
            i.putExtra("name", "Guest User");
            startActivity(i);
        }
    }
}
