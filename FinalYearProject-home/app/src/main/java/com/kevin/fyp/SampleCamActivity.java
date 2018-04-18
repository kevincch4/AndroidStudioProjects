package com.kevin.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.kevin.fyp.R;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SampleCamActivity extends AbstractArchitectCamActivity {

    SeekBar seek_bar;
    //Button screenshot = findViewById(R.id.screenshot);

    CallbackManager callbackManager = CallbackManager.Factory.create();
    ShareDialog shareDialog = new ShareDialog(this);
    File lastModifiedFile;
    ArrayList<File> files;

    /**
     * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
     */
    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

    /**
     * path to the World index.html. Relative paths: Relative to assets-root folder, Absolute paths: Web-Url (http://...) or file-path
     */
    private static String WORLD_PATH = "index.html";

    @Override
    public String getARchitectWorldPath() {
        if (CurrentClothes.currentClothes.equals("purple shirt")) {
            if (CurrentClothes.currentSize.equals("M")) {
                WORLD_PATH = "index.html";
            } else if (CurrentClothes.currentSize.equals("S")) {
                WORLD_PATH = "index_s.html";
            } else if (CurrentClothes.currentSize.equals("L")) {
                WORLD_PATH = "index_l.html";
            }
        } else if (CurrentClothes.currentClothes.equals("polo.wt3")) {
            if (CurrentClothes.currentSize.equals("M")) {
                WORLD_PATH = "polo.html";
            } else if (CurrentClothes.currentSize.equals("S")) {
                WORLD_PATH = "polo_s.html";
            } else if (CurrentClothes.currentSize.equals("L")) {
                WORLD_PATH = "polo_l.html";
            }
        } else if (CurrentClothes.currentClothes.equals("male_shirt.wt3")) {
            if (CurrentClothes.currentSize.equals("M")) {
                WORLD_PATH = "male_shirt.html";
            } else if (CurrentClothes.currentSize.equals("S")) {
                WORLD_PATH = "male_shirt_s.html";
            } else if (CurrentClothes.currentSize.equals("L")) {
                WORLD_PATH = "male_shirt_l.html";
            }

        } else if (CurrentClothes.currentClothes.equals("female_shirt.wt3")) {
            if (CurrentClothes.currentSize.equals("M")) {
                WORLD_PATH = "female_shirt.html";
            } else if (CurrentClothes.currentSize.equals("S")) {
                WORLD_PATH = "female_shirt_s.html";
            } else if (CurrentClothes.currentSize.equals("L")) {
                WORLD_PATH = "female_shirt_l.html";
            }
        }
        return WORLD_PATH;
    }

    @Override
    public String getActivityTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public int getContentViewId() {
        return R.layout.sample_cam;
    }

    @Override
    public int getArchitectViewId() {
        return R.id.architectView;
    }

    @Override
    public String getWikitudeSDKLicenseKey() {
        return WikitudeSDKConstants.WIKITUDE_SDK_KEY;
    }

    @Override
    public ArchitectUrlListener getUrlListener() {
        return new ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                // by default: no action applied when url was invoked
                return false;
            }
        };
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        // you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
        return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
    }

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.sample_cam);
//	}

    public void onButtonClick(View v) {
        if (v.getId() == R.id.S) {
            if (!(CurrentClothes.currentSize.equals("S"))) {
                CurrentClothes.currentSize = "S";
                finish();
                startActivity(getIntent());
            }
        } else if (v.getId() == R.id.M) {
            if (!(CurrentClothes.currentSize.equals("M"))) {
                CurrentClothes.currentSize = "M";
                finish();
                startActivity(getIntent());
            }
        } else if (v.getId() == R.id.L) {
            if (!(CurrentClothes.currentSize.equals("L"))) {
                CurrentClothes.currentSize = "L";
                finish();
                startActivity(getIntent());
            }
        }

    }


    public void onScreenShotClick(View v) {
        if (v.getId() == R.id.screenshot) {
//            final RelativeLayout layout = findViewById(R.id.relalayout);
//            layout.post(new Runnable() {
//                @Override
//                public void run() {
//                    Bitmap picture = takeScreenshot(layout);
//
//                    try {
//                        if (picture != null) {
//                            saveScreenShot(picture);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
            String checkFbLogin = getIntent().getStringExtra("checkFbLogin");
            if(checkFbLogin.equals("Yes")) {
                //find last modified file
                files = new ArrayList<>();
                String d = String.valueOf((Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_PICTURES + "/Screenshots/"));
                files = listf(d, files);

                File finalmodified = checkLastmod(files);

                //put file into bitmap

                Bitmap bitmap = BitmapFactory.decodeFile(finalmodified.getPath());
                SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto).build();
                shareDialog.show(content);

                //create callback
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(SampleCamActivity.this, "Share Successful", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(SampleCamActivity.this, "Share Cancel", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(SampleCamActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }else{
                Log.d("LOGIN",checkFbLogin);
                Log.d("SHARE","Fail");
                Context context = this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("You did not use Facebook Login.");
                builder.setMessage("Would you like to login using your FB account to have the sharing function ?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent i = new Intent(SampleCamActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private Bitmap takeScreenshot(View v) {
        Bitmap screenShot = null;
        try {
            int width = v.getMeasuredWidth();
            int height = v.getMeasuredHeight();

            screenShot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(screenShot);
            v.draw(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenShot;
    }

    private void saveScreenShot(Bitmap bitmap) {
        ByteArrayOutputStream bao = null;
        File file = null;

        try {
            bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, bao);
            file = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/screenshot.png");
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bao.toByteArray());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File checkLastmod(ArrayList<File> files) {
        long latmod = Long.MIN_VALUE;
        lastModifiedFile = null;
        for(File file: files)
        {
            if(file.lastModified() > latmod){
                lastModifiedFile = file;
                latmod = file.lastModified();
            }
        }
        return lastModifiedFile;
    }

    private ArrayList<File> listf(String d, ArrayList<File> files) {
        File directory = new File(d);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath(), files);
            }
        }
        return files;
    }

//    public void seekbarr( ){
//        seek_bar = (SeekBar)findViewById(R.id.seekBar);
//        seek_bar.setOnSeekBarChangeListener(
//                new SeekBar.OnSeekBarChangeListener() {
//                    int progress_value;
//                    int current_value;
//
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        if(progress_value>current_value){
//                            Clothes.scale.x += 0.0001;
//                            Clothes.scale.y += 0.0001;
//                            Clothes.scale.z += 0.0001;
//                        }
//                        if(progress_value<current_value){
//                            Clothes.scale.x -= 0.0001;
//                            Clothes.scale.y -= 0.0001;
//                            Clothes.scale.z -= 0.0001;
//                        }
//                        progress_value = progress;
//                    }
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                    }
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        current_value = progress_value;
//                    }
//                }
//        );
//
//    }


}
