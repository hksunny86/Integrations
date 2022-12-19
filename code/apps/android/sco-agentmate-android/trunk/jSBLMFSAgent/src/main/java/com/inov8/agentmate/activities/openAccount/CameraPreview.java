package com.inov8.agentmate.activities.openAccount;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inov8.agentmate.util.ApplicationData;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    Context app_context;
    private int sensorOrientation;

    public CameraPreview(Context context, Camera camera, int sensorOrientation) {
        super(context);
        mCamera = camera;
        app_context = context;
        this.sensorOrientation = sensorOrientation;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

            // get Camera parameters
            Camera.Parameters params = mCamera.getParameters();
            // params.setPictureFormat(Camera.Parameters.)
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // set the focus mode
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                // set Camera parameters
                if (focusModes
                        .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }
                mCamera.setParameters(params);

                Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean arg0, Camera arg1) {

                    }
                };
                mCamera.autoFocus(mAutoFocusCallback);
            }
            ApplicationData.isCameraPreviewLoaded = true;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // release camera resources
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {

            mCamera.setPreviewDisplay(mHolder);

            final Camera.Parameters parameters = mCamera.getParameters();

            if(app_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
                parameters.setFlashMode(Parameters.FLASH_MODE_ON);
            parameters.set("jpeg-quality", 100);

            mCamera.setParameters(parameters);
            if(sensorOrientation == 270)
                mCamera.setDisplayOrientation(270);
            else
                mCamera.setDisplayOrientation(90);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("Error", "Error starting camera preview: " + e.getMessage());
        }
    }
}