package com.example.androidtest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class CameraActivity extends AppCompatActivity {

    private Button backButton;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout view;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //We initiate the camera functionality, since we already checked the permissions to get to this point
        openCamera();

        //We set the back button functionality to remove the SecondActivity once it's finished

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //we add the event listener to the capture button

        Button captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });

        //We set a touch event listner
        NavigationView nav = findViewById(R.id.navigationView);



        view = findViewById(R.id.camera_preview);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mCamera != null) {
                    mCamera.cancelAutoFocus();
                    Rect focusRect = calculateFocusArea(event.getX(), event.getY(), view.getWidth(), view.getHeight());

                    Camera.Parameters params = mCamera.getParameters();
                    if (params.getMaxNumFocusAreas() > 0) {
                        List<Camera.Area> focusAreas = new ArrayList<>();
                        focusAreas.add(new Camera.Area(focusRect, 1000));
                        params.setFocusAreas(focusAreas);
                    }

                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    mCamera.setParameters(params);
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            // Optionally, add logic for when focus is achieved
                        }
                    });
                }
                return false;

            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //Function to check for the camera permission

    /**
     * User created function that executes the necesary code for camera functionality within the app
     */
    private void openCamera() {

        mCamera = getCameraInstance();
        //We create out preview and load it
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    //We create a function to get the camera object
    public static Camera getCameraInstance() {
        //We declare the variable and set it to null for managing errors
        Camera c = null;

        try {
            c = Camera.open();
            //We activate the camera focus
            Camera.Parameters params = c.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            c.setParameters(params);
            //We catch any exception that might have happened when trying to access the camera
        } catch (Exception e) {
            //Camara is not available or does not exist.
        }


        return c; //Will return the camera object if it's available, otherwise will return null


    }


    //Callback to capture picture
    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            //Once picture is taken, we will go to the next scene to see if the user accepts or rejects the picture

            switchToSelectImage(data);
            finish();
        }


    };


    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera(); // release the camera immediately on pause event
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    //function to go to SelectImage scene
    public void switchToSelectImage(byte[] data) {

        Intent switchActivityIntent = new Intent(this, SelectImage.class);
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        Uri imgUri = saveBitmapToFile(bmp);
        switchActivityIntent.putExtra("imageuri", imgUri.toString());
        startActivity(switchActivityIntent);

    }

    private Uri saveBitmapToFile(Bitmap bitmap) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = new File(storageDir, imageFileName + ".jpg");
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return Uri.fromFile(imageFile);
    }


    private static final int FOCUS_AREA_SIZE = 300;


    private Rect calculateFocusArea(float x, float y, int width, int height) {
        int left = clamp(Float.valueOf((x / width) * 2000 - 1000).intValue() - FOCUS_AREA_SIZE / 2, -1000, 1000);
        int top = clamp(Float.valueOf((y / height) * 2000 - 1000).intValue() - FOCUS_AREA_SIZE / 2, -1000, 1000);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);

    }


    private int clamp(int value, int min, int max) {
        if (value > max) {
            return max;
        }else if(value<min){
            return min;
        }
        return value;
    }
}