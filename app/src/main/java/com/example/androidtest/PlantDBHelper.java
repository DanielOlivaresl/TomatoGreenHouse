package com.example.androidtest;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class PlantDBHelper extends SQLiteOpenHelper {
    //Database information
    private static final String DB_NAME = "plants";
    private static final int DB_VERSION= 1;

    //Table names
    public static final String TABLE_TOMATO ="tomato";

    //Tomato table columns
    public static final String KEY_ID = "id";
    public static final String ESTADO_PLANTA="estado";
    public static final String TEMPERATURA_PLANTA="temperatura";
    public static final String HUMEDAD_PLANTA="humedad";
    public static final String LITROS_AGUA_PLANTA="agua";
    public static final String MAGNESIO_PLANTA="magnesio";
    public static final String POTASIO_PLANA="potasio";
    public static final String FOSFORO_PLANTA="fosforo";
    public static final String CALCIO_PLANTA="calcio";


    public PlantDBHelper(Context context){
        super(context,DB_NAME,null, DB_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
            String CREATE_NOTES_TABLE = ("CREATE TABLE %s (" +
                    " %s INTEGER PRIMARY KEY," +
                    " %s char(10)," +
                    " %s decimal," +
                    " %s decimal," +
                    " %s decimal," +
                    " %s decimal," +
                    " %s decimal," +
                    " %s decimal," +
                    " %s decimal"+
                    " )").formatted(TABLE_TOMATO,KEY_ID,ESTADO_PLANTA,TEMPERATURA_PLANTA,HUMEDAD_PLANTA,LITROS_AGUA_PLANTA,MAGNESIO_PLANTA,POTASIO_PLANA,FOSFORO_PLANTA,CALCIO_PLANTA);

            db.execSQL(CREATE_NOTES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOMATO);
        onCreate(db);
    }
}
