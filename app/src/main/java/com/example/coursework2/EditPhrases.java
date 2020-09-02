package com.example.coursework2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class EditPhrases extends AppCompatActivity {
    ListView listView2;   //declare variables , arraylists and objects
    Button save;
    Button edit;
    EditText userInput2;
    Database database;
    ArrayAdapter adapter;
    ArrayList<String> list2=new ArrayList<>();  // new list after updating values
    String selectItem;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);
        save=findViewById(R.id.save_button);                   // initializing objects
        edit=findViewById(R.id.edit_button);
        userInput2=findViewById(R.id.input2);
        listView2=findViewById(R.id.listview_with_radio_buttons);
        database=new Database(this);


        displayData();       //displaying previously saved words

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

                edit.setOnClickListener(new View.OnClickListener() {    //onclick listner for edit button
                    @Override
                    public void onClick(View v) {
                        selectItem= listView2.getItemAtPosition(position).toString();   //get the name of phrase which is selected
                        userInput2.setText(selectItem);                                  //display selected phrase on textbox.

                    }
                });

            }
        });

    }
    public void displayData(){
        Cursor cursor2=database.getData();
        if (cursor2.getCount()==0){               //check that database is empty or not
            Toast.makeText(this,"No data to display",Toast.LENGTH_SHORT).show();
        }else {
            while (cursor2.moveToNext()){
                list2.add(cursor2.getString(1));     //adding words to a new list , after getting saved words from database

                Collections.sort(list2);   // sorting the list

            }
            adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,list2);   //create adapter and display updated list
            listView2.setAdapter(adapter);            //setting the adapter to listview

        }
    }

    public void saveData(View view) {                 // saving updated data to database

        Cursor cursor = database.getId(selectItem);      //create cursor object and get the
                int id2=0;
                while (cursor.moveToNext()){
                    id2 = cursor.getInt(0);    //getting the positions of words saved in database
                }

                String newword = userInput2.getText().toString().trim();

                if (!newword.matches("")){             //checking that new word is empty or not

                    database.update(newword.toLowerCase(),id2,selectItem);        //updating data

                    list2.clear();            //clear current list
                    displayData();           //display updated list

                    userInput2.setText("");
                    Toast.makeText(getApplicationContext(),"Updated ...",Toast.LENGTH_SHORT).show();
                    userInput2.setText("");


                }else {
                    Toast.makeText(getApplicationContext(),"Empty phrase !!!",Toast.LENGTH_SHORT).show();
                    userInput2.setText("");
                }

            }

}


