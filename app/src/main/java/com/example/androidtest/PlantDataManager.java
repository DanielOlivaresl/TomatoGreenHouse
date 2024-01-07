package com.example.androidtest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlantDataManager {
    private PlantDBHelper dbHelper;

    public PlantDataManager(Context context) {
        dbHelper= new PlantDBHelper(context);

    }

    public void addNote(String id, String state,String temperature,String humidity, String water, String magneseum, String potasium, String fosfurus, String calcium ){
        //Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Create new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PlantDBHelper.KEY_ID,id);
        values.put(PlantDBHelper.ESTADO_PLANTA,state);
        values.put(PlantDBHelper.CALCIO_PLANTA,calcium);
        values.put(PlantDBHelper.FOSFORO_PLANTA,fosfurus);
        values.put(PlantDBHelper.HUMEDAD_PLANTA,humidity);
        values.put(PlantDBHelper.LITROS_AGUA_PLANTA,water);
        values.put(PlantDBHelper.MAGNESIO_PLANTA,magneseum);
        values.put(PlantDBHelper.POTASIO_PLANA,potasium);
        values.put(PlantDBHelper.TEMPERATURA_PLANTA,temperature);

        //Insert the new row, returning the primary key of the new row

        long newRowId= db.insert(PlantDBHelper.TABLE_TOMATO,null ,values);


    }

    public List<Float> getAllNotes() {
        List<Float> res = new ArrayList<>(Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f));

        String selectQuery = "SELECT  * FROM " + PlantDBHelper.TABLE_TOMATO;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count=0;
        if (cursor.moveToFirst()) {

            do {

                for(int i=2; i<cursor.getColumnCount(); i++){
                    float element = res.remove(i-2);
                    res.add(i-2,element + cursor.getFloat(i));

                }
                count++;
            } while (cursor.moveToNext());
        }

        for(int i=0; i<res.size(); i++){
            Float element= res.remove(i);
            res.add(i,element/count);
        }

        cursor.close();
        return res;
    }


    public List<Integer> getPlantStates(){
        List<Integer> counts= new ArrayList<>();
        counts.add(0);
        counts.add(0);
        counts.add(0);


        String selectQuery = "SELECT * FROM " + PlantDBHelper.TABLE_TOMATO;
        SQLiteDatabase db= dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(1).equals("bien")){
                    int val = counts.remove(0);
                    counts.add(0,val+1);
                }else if(cursor.getString(1).equals("en riesgo")){
                    int val = counts.remove(1);

                    counts.add(1,val+1);
                }else{
                    int val = counts.remove(2);

                    counts.add(2,val+1);
                }
            }while(cursor.moveToNext());
        }
        return counts;
    }
}
