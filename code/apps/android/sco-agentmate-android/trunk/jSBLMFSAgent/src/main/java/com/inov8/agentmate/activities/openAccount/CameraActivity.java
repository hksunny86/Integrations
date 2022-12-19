package com.inov8.agentmate.activities.openAccount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsbl.sco.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraActivity extends Activity {

	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final String PARM_ROTATION = "rotation";
	private static final byte FLASH_ON = 1;
	private static final byte FLASH_OFF = 0;
    public static int result=0;
    final private int ERROR = 0;
	private Bundle bundle;
	private Camera camera;
	private FrameLayout frmPreview;
	private CameraPreview camPreview;
	private String photoType, msisdn;
	private ImageButton flashButton;
	private Camera.Parameters params;
	private OrientationEventListener orientationListener;
	private int lastOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
	private int photoThreshold;
	private int flashToggle = 0;
	private int background;
	private float defaultCameraRatio;
	private boolean captureFlag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		bundle = getIntent().getExtras();
		photoType = bundle.getString("PHOTO_TYPE");
		msisdn = bundle.getString(Constants.ATTR_CMSISDN);

		initializeCamera();
        if (result != ERROR) {
            // Add a listener to the Capture button
            ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ApplicationData.isCameraPreviewLoaded) {
                        if (!captureFlag) {
                            int orientation = lastOrientation;
                            if (orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
                                orientation += 90;
                            }

                            orientation = roundOrientation(orientation);

                            params.set(PARM_ROTATION, orientation);
                            camera.setParameters(params);

                            System.gc();
                            long free = Runtime.getRuntime().freeMemory();
                            AppLogger.i("####Free Memory: " + free);
                            Runtime.getRuntime().gc();

                            try {
                                camera.takePicture(null, null, mPicture);
                            } catch (RuntimeException re) {
                                AppLogger.i("####Crashed");
                                setResult(RESULT_CANCELED);
                                CameraActivity.this.finish();
                            } finally {
                                ApplicationData.isCameraPreviewLoaded = false;
                                captureFlag = true;
                            }
                            ApplicationData.isCameraPreviewLoaded = false;
                            captureFlag = true;
                        }
                    }
                }
            });

            flashToggle = FLASH_OFF;
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);

            // Add a listener to the Capture button
            flashButton = (ImageButton) findViewById(R.id.button_flash);
            flashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean hasFlash = getApplicationContext().getPackageManager()
                            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                    if (hasFlash) {
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
                        flashButton.setBackgroundResource(background);
                        camera.setParameters(params);
                    }
                }
            });

            // Create orientation listenter. This should be done first because it
            // takes some time to get first orientation.
            orientationListener = new OrientationEventListener(CameraActivity.this) {
                @Override
                public void onOrientationChanged(int orientation) {
                    // We keep the last known orientation. So if the user
                    // first orient the camera then point the camera to
                    // floor/sky, we still have the correct orientation.
                    if (orientation != ORIENTATION_UNKNOWN) {
                        lastOrientation = orientation;
                    }
                }
            };
            orientationListener.enable();
        }
        else {
            if (camera == null) {
                Intent intent = new Intent();
                intent.putExtra("RESULT", "ERROR");
                setResult(RESULT_CANCELED, intent);
                this.finish();
            }
        }
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			Log.e("AGENTmate", "Failed to open Camera");
			e.printStackTrace();
		}
		return c; // returns null if camera is unavailable
	}

	private boolean pictureFlag = true;
	private PictureCallback mPicture = new PictureCallback() {

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
				}
			}
		}
	};

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

        String file = Environment.getExternalStorageDirectory().getPath();

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"AGENTmateApps");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("AGENTmateApps", "Failed to create directory");
				Toast.makeText(CameraActivity.this,
						"Can't create directory to save image.",
						Toast.LENGTH_LONG).show();
				return null;
			}
		}

		String filename = "";
		if (photoType.equals("customer")) {
			filename = "AGENTmate_" + msisdn + "_Customer_Photo.png";
		} else if (photoType.equals("cnic_front")) {
			filename = "AGENTmate_" + msisdn + "_CNIC_Front_Photo.png";
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

	@Override
	public void onPause() {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
			frmPreview.removeView(camPreview);
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (camera == null) {
			setResult(RESULT_CANCELED);
			this.finish();
		}
		super.onResume();
	}

	private Size getPreferredPictureSize() {
		Size res = null;
		List<Size> sizes = camera.getParameters().getSupportedPictureSizes();

		for (Size s : sizes) {
			float ratio = (float) s.width / (float) s.height;

			if (Math.round(ratio * 100.0) / 100.0 == Math.round(defaultCameraRatio * 100.0) / 100.0
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

	public Size closest(int of, List<Size> in) {
		int min = Integer.MAX_VALUE;
//		int closest = of;
		Size size = null;

		for (Size v : in) {
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

	private int roundOrientation(int orientationInput) {
		int orientation = orientationInput;
		if (orientation == -1) {
			orientation = 0;
		}

		orientation = orientation % 360;
		int retVal = 0;
		if (orientation < (0 * 90) + 45) {
			retVal = 0;
		} else if (orientation < (1 * 90) + 45) {
			retVal = 90;
		} else if (orientation < (2 * 90) + 45) {
			retVal = 180;
		} else if (orientation < (3 * 90) + 45) {
			retVal = 270;
		}

		return retVal;
	}

	private void initializeCamera() {
        try {
            // Create an instance of Camera
            camera = getCameraInstance();
            // get Camera parameters
            params = camera.getParameters();
            defaultCameraRatio = (float) params.getPictureSize().width
                    / (float) params.getPictureSize().height;
            AppLogger.i("##Default Ratio: " + defaultCameraRatio);

            photoThreshold = 384;// , 640;
            Size mSize = getPreferredPictureSize();

            params.setColorEffect(Camera.Parameters.EFFECT_NONE);
            params.setPictureSize(mSize.width, mSize.height);
            params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            params.setPictureFormat(ImageFormat.JPEG);
            // params.setPreviewFrameRate(20);
            camera.setParameters(params);

            String cameraId = null;
            int sensorOrientation = 90;

            CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
            if(manager.getCameraIdList()!=null && manager.getCameraIdList().length>0) {
                cameraId = manager.getCameraIdList()[0];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            }

            // Create our Preview view and set it as the content of our activity.
            camPreview = new CameraPreview(this, camera, sensorOrientation);

            frmPreview = (FrameLayout) findViewById(R.id.camera_preview);
            frmPreview.addView(camPreview);
            result=1;
        }catch (Exception e){
            e.printStackTrace();
            result=0;
            return;
        }
	}
}