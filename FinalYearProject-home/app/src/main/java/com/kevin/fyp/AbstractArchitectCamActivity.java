package com.kevin.fyp;

import java.io.IOException;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.opengl.GLES20;
import android.os.Bundle;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

/**
 * Abstract activity which handles live-cycle events.
 * Feel free to extend from this activity when setting up your own AR-Activity
 *
 */
public abstract class AbstractArchitectCamActivity extends Activity implements ArchitectViewHolderInterface{

    /**
     * holds the Wikitude SDK AR-View, this is where camera, markers, compass, 3D models etc. are rendered
     */
    protected ArchitectView					architectView;

    /**
     * urlListener handling "document.location= 'architectsdk://...' " calls in JavaScript"
     */
    protected ArchitectUrlListener 			urlListener;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

		/* pressing volume up/down should cause music volume changes */
        this.setVolumeControlStream( AudioManager.STREAM_MUSIC );

		/* set samples content view */
        this.setContentView( this.getContentViewId() );

        this.setTitle( this.getActivityTitle() );

		/* set AR-view for life-cycle notifications etc. */
        this.architectView = (ArchitectView)this.findViewById( this.getArchitectViewId()  );

		/* pass SDK key if you have one, this one is only valid for this package identifier and must not be used somewhere else */
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey(getWikitudeSDKLicenseKey());
        config.setFeatures(ArchitectStartupConfiguration.Features.Tracking2D);

		/* first mandatory life-cycle notification */
        this.architectView.onCreate( config );

        // set urlListener, any calls made in JS like "document.location = 'architectsdk://foo?bar=123'" is forwarded to this listener, use this to interact between JS and native Android activity/fragment
        this.urlListener = this.getUrlListener();

        // register valid urlListener in architectView, ensure this is set before content is loaded to not miss any event
        if ( this.urlListener !=null ) {
            this.architectView.registerUrlListener( this.getUrlListener() );
        }
    }

    @Override
    protected void onPostCreate( final Bundle savedInstanceState ) {
        super.onPostCreate(savedInstanceState);

        if ( this.architectView != null ) {

            // call mandatory live-cycle method of architectView
            this.architectView.onPostCreate();

            try {
                // load content via url in architectView, ensure '<script src="architect://architect.js"></script>' is part of this HTML file, have a look at wikitude.com's developer section for API references
                this.architectView.load( this.getARchitectWorldPath() );

                if (this.getInitialCullingDistanceMeters() != ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS) {
                    // set the culling distance - meaning: the maximum distance to render geo-content
                    this.architectView.setCullingDistance( this.getInitialCullingDistanceMeters() );
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // call mandatory live-cycle method of architectView
        if ( this.architectView != null ) {
            this.architectView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // call mandatory live-cycle method of architectView
        if ( this.architectView != null ) {
            this.architectView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // call mandatory live-cycle method of architectView
        if ( this.architectView != null ) {
            this.architectView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if ( this.architectView != null ) {
            this.architectView.onLowMemory();
        }
    }

    /**
     * title shown in activity
     * @return
     */
    public abstract String getActivityTitle();

    /**
     * path to the architect-file (AR-Experience HTML) to launch
     * @return
     */
    @Override
    public abstract String getARchitectWorldPath();

    /**
     * url listener fired once e.g. 'document.location = "architectsdk://foo?bar=123"' is called in JS
     * @return
     */
    @Override
    public abstract ArchitectUrlListener getUrlListener();

    /**
     * @return layout id of your layout.xml that holds an ARchitect View, e.g. R.layout.camview
     */
    @Override
    public abstract int getContentViewId();

    /**
     * @return Wikitude SDK license key, checkout www.wikitude.com for details
     */
    @Override
    public abstract String getWikitudeSDKLicenseKey();

    /**
     * @return layout-id of architectView, e.g. R.id.architectView
     */
    @Override
    public abstract int getArchitectViewId();

    /**
     * helper to check if video-drawables are supported by this device. recommended to check before launching ARchitect Worlds with videodrawables
     * @return true if AR.VideoDrawables are supported, false if fallback rendering would apply (= show video fullscreen)
     */
    public static final boolean isVideoDrawablesSupported() {
        String extensions = GLES20.glGetString( GLES20.GL_EXTENSIONS );
        return extensions != null && extensions.contains( "GL_OES_EGL_image_external" ) && android.os.Build.VERSION.SDK_INT >= 14 ;
    }

}