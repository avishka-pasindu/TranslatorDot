package com.example.coursework2;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.List;

public class LanguageSubscription extends AppCompatActivity {
    ListView listView3;
    Button update;
    ArrayAdapter adapter3;
    Database database;
    SparseBooleanArray idSel;
    List<IdentifiableLanguage> list3=new ArrayList<>();
    ArrayList<String> list4=new ArrayList<>();
    ArrayList<String> selectedLanguages=new ArrayList<>();
    ArrayList<Integer> selectedPositions=new ArrayList<Integer>();
    ArrayList<Integer> listCheckedPositions=new ArrayList<Integer>();
    ArrayList<String>  selectedLangCodes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        database=new Database(this);
        listView3=findViewById(R.id.list_languages);
        update=findViewById(R.id.updateButton);

        Async async=new Async();
        async.execute();             //executing async

        displayData();            //display languages


        Cursor cursor=database.getSelectedPosition();
        while (cursor.moveToNext()){
            listCheckedPositions.add(cursor.getInt(2));     //get the positions of selected items and add them to a list
        }
        for (int v=0;v<listCheckedPositions.size();v++){
            listView3.setItemChecked(listCheckedPositions.get(v),true);     //set the items checked which are selected by acessing listCheckedPositions list
        }

        database.deleteData();

        while (cursor.moveToNext()){
            listCheckedPositions.add(cursor.getInt(2));       // subscribing new languages again
        }
        for (int v=0;v<listCheckedPositions.size();v++){
            listView3.setItemChecked(listCheckedPositions.get(v),true);
        }


        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String value="";
                        int x=0;

                        idSel=listView3.getCheckedItemPositions();  //add checked items positions to spareseboolean array

                        int  i=0;

                        Cursor cursor5=database.getCode();      //get the language codes from table 2
                        while (cursor5.moveToNext()){
                           selectedLangCodes.add(cursor5.getString(2));   // add them to an arraylist
                        }


                      while (i<idSel.size()){

                          if (idSel.valueAt(i)){                  // true ,if checked
                              value=list4.get(idSel.keyAt(i));       //get the language  name
                              x=idSel.keyAt(i);                   //get the position
                              String code=selectedLangCodes.get(idSel.keyAt(i));         //get the language code
                              selectedLanguages.add(value);
                              selectedPositions.add(idSel.keyAt(i));
                              database.addSelectedLang(value,x,code);


                          }
                          i++;
                      }

                        Toast.makeText(getApplicationContext()," Subscribed selected languages... " ,Toast.LENGTH_SHORT).show();
                        selectedPositions.clear();     // clear the arraylists
                        selectedLanguages.clear();
                        selectedLangCodes.clear();

                    }

                });
            }
        });

    }


    public void displayData(){
        Cursor cursor3=database.getLang();    //get languages from table 2
        while (cursor3.moveToNext()){
                list4.add(cursor3.getString(1));  // add them to arraylist

        }
            adapter3=new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,list4);
            listView3.setAdapter(adapter3);

    }





    private class Async extends AsyncTask{
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            System.out.println(list3);
    for (int i=0;i<list3.size();i++){
        IdentifiableLanguage il=list3.get(i);
        String name= il.getName();                   //language name
        String name2=il.getLanguage();               //language code
        database.addLang(name,name2);               // adding them to database by using method addLang created in Database.java

    }

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            IamAuthenticator authenticator = new IamAuthenticator("t5gRr_kSJmupsnd9AhVsX_oELls9tC0woh-RT1Yrh2dg");    //unique api key

            LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);

            languageTranslator.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/8b32c82f-ffe5-45c9-815e-56f54c8afde2");  //unique url

            IdentifiableLanguages languages = languageTranslator.listIdentifiableLanguages()
                    .execute().getResult();
            list3 =languages.getLanguages();      //arraylist that contains all languages and language codes
            return list3;

        }
    }
}

