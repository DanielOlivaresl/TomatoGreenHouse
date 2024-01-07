package com.example.androidtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.nfc.Tag;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;

import java.io.IOException;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

//Class so that the user can have a preview of the camera
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;


    public CameraPreview(Context context,Camera camera) {
        super(context);
        mCamera = camera;


        //Install a SurfaceHolder.callback so we get notified when the
        //underlying sufrace is created and destroyed

        mHolder = getHolder();
        mHolder.addCallback(this);


        //Depreciated Setting, but required for android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }






    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //The surface has been created, now tell the camera whre to draw the preview
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        }catch(IOException e){
            Log.d(TAG,"Erro setting camera preview: " + e.getMessage());
        }
        //We rotate the camera
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        //Code for rotating or changing

        if(mHolder.getSurface()==null){
            //preview surface does not exist
            return;
        }


        //Stop preview before making changes

        try{
            mCamera.stopPreview();
        }catch (Exception e){
            //ignore: tried to stop a non-extistent value
        }

        //Set preview size and make any resize, rotate or reformatting changes here


        //start preview with new settings

        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch(Exception e){
            Log.d(TAG, "Error starting camera preview " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        //Empty take care of releasing the camera preview in your activity
    }
}
