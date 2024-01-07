package com.example.androidtest;

import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InformsActivity extends AppCompatActivity {

    @RequiresApi(api = 34)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informs);




        //Using a Local DB

        PlantDataManager dataManager= new PlantDataManager(this);


        //We will simulate results given from id3 algorithm and diverse sensors, to do this we will add 10 plants with random data
        Random rand = new Random();



//        for(int i=0; i<100; i++){
//           int randInt= rand.nextInt()%3;
//           String state;
//           if(randInt==0){
//               state= "bien";
//           }else if(randInt==1){
//               state="puede mejorar";
//           }else{
//               state= "en riesgo";
//           }
//
//            dataManager.addNote(
//                    Integer.toString(i+1),
//                    state,
//                    Float.toString((rand.nextFloat()%100)*100),
//                    Float.toString((rand.nextFloat()%100)*100),
//                    Float.toString((rand.nextFloat()%100)*100),
//                    Float.toString((rand.nextFloat()%100)*100),
//                    Float.toString((rand.nextFloat()%100)*100),
//                    Float.toString((rand.nextFloat()%100)*100),
//                    Float.toString((rand.nextFloat()%100)*100)
//                    );
//        }



        List<Float> estados = dataManager.getAllNotes();
        //We display the results in a table


        TableLayout table = findViewById(R.id.tableID);
        List<TableRow> tableRows = new ArrayList<>();
        tableRows.add(findViewById(R.id.tableRow1));
        tableRows.add(findViewById(R.id.tableRow2));
        tableRows.add(findViewById(R.id.tableRow3));
        tableRows.add(findViewById(R.id.tableRow4));
        tableRows.add(findViewById(R.id.tableRow5));
        tableRows.add(findViewById(R.id.tableRow6));
        tableRows.add(findViewById(R.id.tableRow7));


       for(int i=0; i<tableRows.size(); i++){
           TextView textView = new TextView(this);
            textView.setText(Float.toString(estados.get(i)));

                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10,10,10,10);
                textView.setBackground(ContextCompat.getDrawable(this,R.drawable.cell_border));
                tableRows.get(i).addView(textView);

            table.removeView(tableRows.get(i));
            table.addView(tableRows.get(i));
        }



       List<Integer> states= dataManager.getPlantStates();

       PieChart pieChart = findViewById(R.id.pieChart);

       ArrayList<PieEntry> entries = new ArrayList<>();





        entries.add(new PieEntry(((float) states.get(0) /(states.get(0)+ states.get(1)+ states.get(2)))*100,"Plantas Sanas"));
        entries.add(new PieEntry(((float) states.get(1) /(states.get(0)+ states.get(1)+ states.get(2)))*100,"En Riesgo"     ));
        entries.add(new PieEntry(((float) states.get(2) /(states.get(0)+ states.get(1)+ states.get(2)))*100,"Pueden Mejorar" ));

        PieDataSet pds=  new PieDataSet(entries,"Plantas");
        pds.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pd = new PieData(pds);
        pieChart.setData(pd);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);

        pieChart.invalidate();





    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}