package com.example.coursework2;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;
import java.util.Collections;


public class Translate extends AppCompatActivity {
Database database;
ArrayList<String> listWords;
ArrayAdapter adapter;
ListView listView;
Spinner spinner;
Button translate;
Button pronounce;
TextView translatedWord;
ArrayList<String> langArray;
    ArrayList<String> langCodeArray;
String selectedWord;
String selectedLanguage;
    String selectedLangCode;
    LanguageTranslator translationService;
    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);


        listView=findViewById(R.id.saved_words);
        database=new Database(this);
        listWords=new ArrayList<>();
        langArray=new ArrayList<>();
        langCodeArray=new ArrayList<>();
        spinner=findViewById(R.id.spinner);
        translate=findViewById(R.id.translate);
        pronounce=findViewById(R.id.pronounce);
        translatedWord=findViewById(R.id.translated_word);
        textService = initTextToSpeechService();


        displayData();            // display saved phrases

        Cursor cursor=database.getSelectedPosition();           //getting subscribed phrases and add them to arraylist
        while (cursor.moveToNext()){
            langArray.add(cursor.getString(1));
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, langArray );
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);                  //display subscribed languages in a spinner


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedWord= listView.getItemAtPosition(position).toString();          //store selected phrase in a variable

            }
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)spinner.getSelectedView()).setTextSize(21);     // change text size of spinner text

                Cursor cursorCode=database.getSelectedPosition();
                while (cursorCode.moveToNext()){
                    langCodeArray.add(cursorCode.getString(3));      //get language code
                }
                selectedLangCode=langCodeArray.get(position);  //get language code matches for the language selected

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void displayData(){                       //display phrases
        Cursor cursor2=database.getData();
        if (cursor2.getCount()==0){
            Toast.makeText(this,"No data to display",Toast.LENGTH_SHORT).show();
        }else {
            while (cursor2.moveToNext()){
                listWords.add(cursor2.getString(1));

                Collections.sort(listWords);

            }
            adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,listWords);
            listView.setAdapter(adapter);

        }
}

    public void translate(View view) {

        translationService = initLanguageTranslatorService();      //execute translation
        new TranslationTask().execute(selectedWord);     //set selected phrase

}

    public void pronounceWord(View view) {

        String translatedResult=translatedWord.getText().toString();     //execute pronouncing
        new SynthesisTask().execute(translatedResult);
    }


    private class TranslationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(params[0])
                    .source(Language.ENGLISH)
                    .target(selectedLangCode)            //set selected language code
                    .build();
            TranslationResult result = translationService.translate(translateOptions).execute().getResult();
            String firstTranslation = result.getTranslations().get(0).getTranslation();
            return firstTranslation;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            translatedWord.setText(s);
        }
    }


    private LanguageTranslator initLanguageTranslatorService() {
        IamAuthenticator authenticator = new IamAuthenticator("t5gRr_kSJmupsnd9AhVsX_oELls9tC0woh-RT1Yrh2dg");
        LanguageTranslator service1 = new LanguageTranslator("2018-05-01", authenticator);
        service1.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/8b32c82f-ffe5-45c9-815e-56f54c8afde2");
        return service1;
    }



    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator("AT1Txid0RJAXB25zFjdMwKdEsc57RV_8rbg0olZUZkTN");
        TextToSpeech service2 = new TextToSpeech(authenticator);
        service2.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/756cafd8-c82a-4fee-8dba-f157302a5889");
        return service2;
    }


    private class SynthesisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build();
            player.playStream(textService.synthesize(synthesizeOptions).execute()
                    .getResult());
            return "Did synthesize";
        }



    }}



