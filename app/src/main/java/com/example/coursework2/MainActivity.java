package com.example.coursework2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Intent intent;    //creating intent object
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addPhrases(View view) {
       intent=new Intent(MainActivity.this,AddPhrases.class);           // initializing intent object
       startActivity(intent);                                                        //pass intent object

    }

    public void displayPhrases(View view) {
        intent=new Intent(MainActivity.this,DisplayPhrases.class);
        startActivity(intent);
    }

    public void editPhrases(View view) {
        intent=new Intent(MainActivity.this, EditPhrases.class);
        startActivity(intent);
    }

    public void languageSubscription(View view) {
        intent=new Intent(MainActivity.this,LanguageSubscription.class);
        startActivity(intent);
    }

    public void translate(View view) {
        intent=new Intent(MainActivity.this,Translate.class);
        startActivity(intent);

    }

}
