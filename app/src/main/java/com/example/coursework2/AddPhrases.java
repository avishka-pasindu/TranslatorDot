package com.example.coursework2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPhrases extends AppCompatActivity {
    EditText input;
    Button saveBtn;
    Database database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);
        input=findViewById(R.id.userInput);
        saveBtn=findViewById(R.id.savePhrase);



    }

    public void savePhrase(View view) {
           String userInput=input.getText().toString().trim();
           database=new Database(this);                       //initialize database object
           if (!userInput.matches("")){                     //check wheather the entered word is empty or not
               database.addData(userInput.toLowerCase());                  //add data SQLite database using  addData method in Database.java

               Toast.makeText(getApplicationContext(),"Successfully saved ...",Toast.LENGTH_SHORT).show();
               input.setText("");                           //make textbox blank after saving the word
           }else {
               Toast.makeText(getApplicationContext(),"Empty phrase !!!",Toast.LENGTH_SHORT).show();
               input.setText("");
           }

    }


}

