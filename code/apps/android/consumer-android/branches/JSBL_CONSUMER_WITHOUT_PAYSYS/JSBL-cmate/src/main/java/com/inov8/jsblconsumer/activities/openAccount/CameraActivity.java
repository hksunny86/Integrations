package com.inov8.jsblconsumer.activities.openAccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class CameraActivity extends AppCompatActivity {

    private final byte FLASH_ON = 1;
    private final byte FLASH_OFF = 0;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private ImageButton  button_flash;
    private AppCompatImageView switchCamera;
    private Context myContext;
    private boolean cameraFront = false;
    private int flashToggle = 0;
    private Camera.Parameters params;

    private String photoType, msisdn;

    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        bundle = getIntent().getExtras();
        photoType = bundle.getString("PHOTO_TYPE");
        msisdn = bundle.getString(Constants.ATTR_CMSISDN);
        cameraFront = bundle.getBoolean(Constants.IntentKeys.OPEN_FRONT_CAMERA);

        myContext = this;
        initialize();



    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                button_flash.setBackgroundResource(R.drawable.flash_off);
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {
            //if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }
//			mCamera = Camera.open(findBackFacingCamera());
            chooseCamera();
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);

            flashToggle = FLASH_OFF;
            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
        }
    }

    public void initialize() {
        LinearLayout cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        ImageButton capture = (ImageButton) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);

        switchCamera = (AppCompatImageView) findViewById(R.id.button_ChangeCamera);
        switchCamera.setOnClickListener(switchCameraListener);

        button_flash = (ImageButton) findViewById(R.id.button_flash);
        button_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                params = mCamera.getParameters();
                boolean hasFlash = getApplicationContext().getPackageManager()
                        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

                if (hasFlash) {
                    int background = R.drawable.flash_off;
                    switch (flashToggle) {
                        case FLASH_ON:
                            flashToggle = FLASH_OFF;
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            background = R.drawable.flash_off;
                            break;
                        case FLASH_OFF:
                            flashToggle = FLASH_ON;
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                            background = R.drawable.flash_on;
                            break;
                    }
                    button_flash.setBackgroundResource(background);
                    mCamera.setParameters(params);
                }
            }
        });
    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get the number of cameras
            int camerasNumber = Camera.getNumberOfCameras();
            if (camerasNumber > 1) {
                //release the old camera instance
                //switch camera, from the front and the back and vice versa

                cameraFront = !cameraFront;

                releaseCamera();
                chooseCamera();
            } else {
                Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
                button_flash.setVisibility(View.VISIBLE);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
                button_flash.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        //check if the device has camera
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean pictureFlag = true;

    private Camera.PictureCallback getPictureCallback() {
        return new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                if (pictureFlag) {
                    pictureFlag = false;
                    File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                    if (pictureFile == null) {
                        AppLogger.i("Error creating media file, check storage permissions ");
                        Toast.makeText(CameraActivity.this,
                                "Please Insert SD Card", Toast.LENGTH_SHORT).show();
                        finish();
                        pictureFlag = true;
                        return;
                    } else {
                        try {
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            AppLogger.i("File not found: " + e.getMessage());
                        } catch (IOException e) {
                            AppLogger.i("Error accessing file: " + e.getMessage());
                        }

                        Intent intent = new Intent();
                        Uri dataUri = Uri.parse(pictureFile.getAbsolutePath());
                        intent.setData(dataUri);
                        intent.putExtra("PATH", pictureFile.getAbsolutePath());
                        setResult(Activity.RESULT_OK, intent);

                        finish();
                        pictureFlag = true;
                        mPreview.refreshCamera(mCamera);
                    }
                }
//
//				Intent intent = new Intent();
//				intent.putExtra("data", data);
//				setResult(Activity.RESULT_OK, intent);
//				finish();
//				mPreview.refreshCamera(mCamera);
            }
        };
    }

    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Camera.Parameters params = mCamera.getParameters();

            if (cameraFront)
                params.setRotation(90);
            else
                params.setRotation(270);
            mCamera.setParameters(params);
            mCamera.takePicture(null, null, mPicture);
        }
    };

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent();
//        intent.putExtra("RESULT", "ERROR");
//        setResult(RESULT_CANCELED, intent);
        this.finish();
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        String file = Environment.getExternalStorageDirectory().getPath();

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "JS_Consumer_Pictures");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("JS_Consumer_Pictures", "Failed to create directory");
                Toast.makeText(CameraActivity.this,
                        "Can't create directory to save image.",
                        Toast.LENGTH_LONG).show();
                return null;
            }
        }

        String filename = "";
        if (photoType.equals("customer")) {
            filename = "CustApp_" + msisdn + "_Customer_Photo.png";
        } else if (photoType.equals("cnic_front")) {
            filename = "CustApp_" + msisdn + "_CNIC_Front_Photo.png";
        }

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getAbsolutePath()
                    + File.separator + filename);
        } else {
            return null;
        }

        return mediaFile;
    }
}