package com.inov8.jsblconsumer.activities.openAccount;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inov8.jsblconsumer.util.AppLogger;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private float defaultCameraRatio;
    private int photoThreshold;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // set the focus mode
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }

            // set Camera parameters
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            defaultCameraRatio = (float) params.getPictureSize().width
                    / (float) params.getPictureSize().height;
            AppLogger.i("##Default Ratio: " + defaultCameraRatio);

            photoThreshold = 320;// , 1280;
            Camera.Size mSize = getPreferredPictureSize();

            params.setColorEffect(Camera.Parameters.EFFECT_NONE);
            params.setPictureSize(mSize.width, mSize.height);
            params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            params.setPictureFormat(ImageFormat.JPEG);

            mCamera.setParameters(params);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(mCamera);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release();
    }

    private Camera.Size getPreferredPictureSize() {
        Camera.Size res = null;
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPictureSizes();

        for (Camera.Size s : sizes) {
            float ratio = (float) s.width / (float) s.height;

            if (Math.round(ratio * 100.0) / 100.0 == Math
                    .round(defaultCameraRatio * 100.0) / 100.0
                    && s.height <= photoThreshold) {
                res = s;
                break;
            }
        }
        // 2nd Algo in-case no resolution matches the criteria
        if (res == null) {
            res = closest(photoThreshold, sizes);
        }
        return res;
    }

    public Camera.Size closest(int of, List<Camera.Size> in) {
        int min = Integer.MAX_VALUE;
        Camera.Size size = null;

        for (Camera.Size v : in) {
            final int diff = Math.abs(v.height - of);
            float ratio = (float) v.width / (float) v.height;

            if (diff < min
                    && Math.round(ratio * 100.0) / 100.0 == Math
                    .round(defaultCameraRatio * 100.0) / 100.0) {
                min = diff;
                size = v;
            }
        }

        return size == null ? in.get(0) : size;
    }
}