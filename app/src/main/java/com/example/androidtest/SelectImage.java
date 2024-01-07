package com.example.androidtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.util.Date;
import java.util.Locale;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class SelectImage extends AppCompatActivity {
    private Bitmap finalImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        //Show the image taken
        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageuri"));

        ImageView imageView = findViewById(R.id.imageView3);
        try {
            Bitmap data = getBitmapFromUri(imageUri);
            Matrix matrix = new Matrix();

            matrix.setRotate(90, data.getWidth()/2, data.getHeight()/2);

            finalImage = Bitmap.createBitmap(data,0,0,data.getWidth(),data.getHeight(),matrix,true);

            imageView.setImageBitmap(finalImage);



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void showImagePreview(Bitmap bitmap){
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }







    //Methods for the buttons

    public void accept(View view){
    //User accepted we save the image to the gallery
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        finalImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
        saveImage(baos.toByteArray());
        Intent switchActivityIntent = new Intent(this, CameraActivity.class);
        startActivity(switchActivityIntent);
        finish();

    }

    public void reject(View view){

        Intent switchActivityIntent = new Intent(this, CameraActivity.class);
        startActivity(switchActivityIntent);
        finish();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private void addPictureToGallery(String path){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    //Converting the uri back to bmp



    //Save image on users request
    private void saveImage(byte[] data){
        File pictureFile = getOutputMediaFile();


        if(pictureFile==null){
            Log.d(TAG,"Error creating media file, check storage permissions");
            return;
        }


        try{
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            addPictureToGallery(pictureFile.getAbsolutePath());
        }catch (FileNotFoundException e){
            Log.d(TAG,"File not found: " + e.getMessage());
        }catch (IOException e){
            Log.d(TAG,"Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile() {
        // Use the Environment's directory for pictures, and create a subdirectory
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}