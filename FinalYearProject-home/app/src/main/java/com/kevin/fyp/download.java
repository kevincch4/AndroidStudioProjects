package com.kevin.fyp;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kevin on 3/4/2018.
 */

public class download extends AppCompatActivity {
    private Button button;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> files_on_server = new ArrayList<>();
    private Handler handler;
    private String selected_file;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        permission_check();
    }

    private void permission_check() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
        }
        initialize();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initialize();
        } else {
            permission_check();
        }
    }

    private void initialize() {
        button = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, files_on_server);
        listView.setAdapter(arrayAdapter);
        handler = new Handler();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //get the list of item in the web server
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("http://101.78.175.101:20680/test/script.php?list_files").build();

                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                            JSONArray array = new JSONArray(response.body().string());

                            //show the list item in web server
                            for (int i = 0; i < array.length(); i++) {
                                String file_name = array.getString(i);

                                //this check the item is in the list already or not, remove duplicate
                                if (files_on_server.indexOf(file_name) == -1) {
                                    files_on_server.add(file_name);
                                }
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                t.start();
            }
        });

        //download the selected file
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selected_file = ((TextView)view).getText().toString();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("http://101.78.175.101:20680/test/files" + selected_file).build();
                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                            float file_size = response.body().contentLength();
                            //donwload the model using stream
                            BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
                            OutputStream stream = new FileOutputStream(Environment.getExternalStorageDirectory()+"/download/"+selected_file);

                            byte[] data = new byte[8192];
                            float total = 0;
                            int read_bytes = 0;

                            //show the progress
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.show();
                                }
                            });

                            //keep updating the download progress
                            while ( (read_bytes = inputStream.read(data)) != -1 ){
                                total = total + read_bytes;
                                stream.write( data, 0, read_bytes);
                                progressDialog.setProgress((int) ((total / file_size)*100));
                            }

                            progressDialog.dismiss();
                            stream.flush();
                            stream.close();
                            response.body().close();



                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.d("Downloaded", selected_file);
                        //update the clothes activity_gallery
                        ArrayList<String> clothes = Clothes.getName();
                        clothes.add(selected_file);



                    }
                });
                t.start();
                Toast.makeText(getApplicationContext(), selected_file+" Downloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
