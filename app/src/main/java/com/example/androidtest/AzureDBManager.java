package com.example.androidtest;

import androidx.annotation.RequiresApi;

import java.sql.*;
import java.util.List;

public class AzureDBManager {

    private final String url="jdbc:sqlserver://tomatogreenhouseserver.database.windows.net:1433;database=TomatoesDB;";
    private final String user="Solif2002@tomatogreenhouse";
    private final String password= "Starwars.663";
    public Connection sqlDBConnection;

    //Tomato Table information

    private final String TABLE_TOMATO ="Tomates";

    private final String ID = "id";
    private final String ESTADO = "estado";
    private final String TEMPERATURA = "temperatura";
    private final String HUMEDAD = "humedad";
    private final String AGUA = "ague";
    private final String MAGNESIO = "magnesio";

    private final String POTASIO = "potasio";
    private final String FOSFORO = "fosforo";
    private final String CALCIO = "calcio";


    public AzureDBManager() {
        try{
            sqlDBConnection= DriverManager.getConnection(url,user,password);

        }catch(SQLException e){
            System.out.println("Error conecting to the Database");
            e.printStackTrace();
        }

    }

    //We will add CRUD functionality (Create, Read, Update & Delete)

    @RequiresApi(api = 34)
    public void addTomato(String state, float temp, float hum, float wat, float mag, float pot, float fos, float cal){

        //We create a statement so that it can be executed
        Statement statement = sqlDBConnection.createStatement();

        //Query to select the max id (i.e. the last element)
        String idSqlQuerry = "SELECT * FROM %s".formatted(TABLE_TOMATO) +
                "where (select max(%s) from %s = %s".formatted(ID,TABLE_TOMATO,ID);

        ResultSet rs= statement.executeQuery(idSqlQuerry);

        String sql = "INSERT INTO %s values( ".formatted(TABLE_TOMATO) +
                "%s".formatted()
                "%s".formatted(state) +
                "%f".formatted(temp) +
                "%f".formatted(hum) +
                "%f".formatted(wat) +
                "%f".formatted(mag) +
                "%f".formatted(pot) +
                "%f".formatted(fos) +
                "%f".formatted(cal) +
                ");";
    }
}
