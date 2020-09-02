package com.example.coursework2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class DisplayPhrases extends AppCompatActivity {
    ListView listWords;
    ArrayList<String> arrayList;     //arraylist to store saved words in database
    ArrayAdapter arrayAdapter;
    Database database;
    TextView count2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        listWords=findViewById(R.id.list_of_phrases);
        database=new Database(this);             //initializing database object
        arrayList=new ArrayList<>();
        count2=findViewById(R.id.count);

        showData();   //display saved phrases

    }
    public void showData(){


        Cursor cursor1=database.getData();                      //call getData method to retrieve data from database
        String count = String.valueOf(cursor1.getCount());       //get the number of words saved in database
        count2.setText(count+" phrases");
        if (cursor1.getCount()==0){                    //check wheather database is empty or not
            Toast.makeText(this,"No data to display",Toast.LENGTH_SHORT).show();
        }else {
            while (cursor1.moveToNext()){
                arrayList.add(cursor1.getString(1));         //add words saved in database to a arraylist
                Collections.sort(arrayList);                         //sorting list in alphabetical order

            }
            arrayAdapter=new ArrayAdapter(this,R.layout.word_display,arrayList);
            listWords.setAdapter(arrayAdapter);         //set arrayadapter
        }
    }

}
