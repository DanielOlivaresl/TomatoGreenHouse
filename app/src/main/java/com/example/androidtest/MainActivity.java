package com.example.androidtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA=1;

    //Initializes application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //On takeFoto, method to be executed once the user wants to take foto

    public void onTakeFoto(View view){

        //We check for camera permissions
        if(checkCameraPermissionAndOpenCamera()){
            //No access, we ask the user for access to the camera
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_CAMERA);
        }else{
            //We give the user access to the next scene
            Intent switchActivityIntent = new Intent(this, CameraActivity.class);
            startActivity(switchActivityIntent);
        }



    }


    public void onGetInform(View view){
        Intent switchActivityIntent = new Intent(this,InformsActivity.class);
        startActivity(switchActivityIntent);
    }

    private boolean checkCameraPermissionAndOpenCamera(){
        //We check for the camera permissions
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Permission was granted we can go to the next scene
            Intent switchActivityIntent = new Intent(this, CameraActivity.class);
            startActivity(switchActivityIntent);
            finish();
        } else {
            Toast.makeText(this, "No permision to the camera, please grant acess", Toast.LENGTH_SHORT).show();

            //Permission denied, show a message
        }
    }

}

