package com.kevin.fyp;

import android.hardware.SensorManager;
import android.location.LocationListener;
import android.util.Log;
import android.widget.Toast;

import com.kevin.fyp.R;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

public class SampleCamActivity extends AbstractArchitectCamActivity {	

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
		if(CurrentClothes.currentClothes.equals("purple shirt")){
			Log.d("WORLD PATH","purple ");
			WORLD_PATH = "index.html";
		}else if (CurrentClothes.currentClothes.equals("polo.wt3")){
			Log.d("WORLD PATH","polo ");
			WORLD_PATH = "polo.html";
		}else if (CurrentClothes.currentClothes.equals("male_shirt.wt3")){
			Log.d("WORLD PATH","male");
			WORLD_PATH = "male_shirt.html";
		}else if (CurrentClothes.currentClothes.equals("female_shirt.wt3")){
			Log.d("WORLD PATH","female");
			WORLD_PATH = "female_shirt.html";
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

}