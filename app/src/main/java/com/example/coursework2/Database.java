package com.example.coursework2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=24;
    private static final String DATABASE_NAME="storePhrases";// Database Name...
    private static final String TABLE_NAME = "Phrases"; // Name of the Table 1...
    private static final String KEY_ID = "id"; // Column1 of the table1
    private static final String KEY_NAME="Word";// Column2 of the table1

    private static final String TABLE_NAME2 = "Languages"; // Name of the Table 2...
    private static final String LANGUAGE_ID = "Language_Id"; // Column1 of the table2
    private static final String LANGUAGE_NAME="Language_Name";// Column2 of the table2
    private static final String LANGUAGE_CODE="Language_Code";// Column3 of the table2


    private static final String TABLE_NAME3 = "Selected_Languages"; // Name of the Table 3...
    private static final String SELECTED_ID = "Selected_Language_Id"; // Column1 of the table3
    private static final String SELECTED_NAME="Selected_Language_Name";// Column2 of the table3
    private static final String SELECTED_POSITION="Selected_Language_Position";// Column3 of the table3
    private static final String SELECTED_CODE="Selected_Language_Code";// Column4 of the table3

    public Database(Context context) {
       super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          String DATABASE_CREATE = "create table "                       //query for creating table 1
                + TABLE_NAME + "(" + KEY_ID
                + " integer primary key autoincrement, " + KEY_NAME
                + " text not null);";


        String DATABASE_CREATE2 = "create table "                         //query for creating table 2
                + TABLE_NAME2 + " (" + LANGUAGE_ID
                + " integer primary key autoincrement, " + LANGUAGE_NAME
                + " text not null, " + LANGUAGE_CODE + " text not null);";


        String DATABASE_CREATE3 = "create table "                          //query for creating table 3
                + TABLE_NAME3 + " (" + SELECTED_ID
                + " integer primary key autoincrement, " + SELECTED_NAME
                + " text not null, " + SELECTED_POSITION + " integer not null, " + SELECTED_CODE + " text not null);";


        db.execSQL(DATABASE_CREATE);      //executing above queries
        db.execSQL(DATABASE_CREATE2);
        db.execSQL(DATABASE_CREATE3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);                          //drop tables when upgrading
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(db);                                              //call oncreate method again , to create tables

    }

    public void addData(String word){                               //to add phrases
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(KEY_NAME,word);
        sqLiteDatabase.insert(TABLE_NAME,null,cv);
        sqLiteDatabase.close();

    }

    public void addLang(String word,String word2){                       //to add languages and codes
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(LANGUAGE_NAME,word);
        cv.put(LANGUAGE_CODE,word2);
        sqLiteDatabase.insert(TABLE_NAME2,null,cv);
        sqLiteDatabase.close();

    }

    public void addSelectedLang(String word,int pos,String code){                //to add checked language, position and code
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(SELECTED_NAME,word);
        cv.put(SELECTED_POSITION,pos);
        cv.put(SELECTED_CODE,code);
        sqLiteDatabase.insert(TABLE_NAME3,null,cv);
        sqLiteDatabase.close();

    }


    public  Cursor getData(){                                             //to get phrases
     SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
     String query="SELECT * FROM " +TABLE_NAME;
     Cursor cursor=sqLiteDatabase.rawQuery(query,null);
     return cursor;

    }


    public  Cursor getLang(){                                          //to get languages
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query="SELECT * FROM " +TABLE_NAME2 +" WHERE " +LANGUAGE_ID+ " LIMIT 75";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        return cursor;

    }


    public  Cursor getSelectedPosition(){                               //to get selected position
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query="SELECT * FROM " +TABLE_NAME3;
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        return cursor;

    }


    public void update(String newWord, int id,  String word){               //update phrases
        SQLiteDatabase database = this.getWritableDatabase();
        String q = " UPDATE " + TABLE_NAME + " SET " + KEY_NAME + " = '" + newWord +  "' WHERE " + KEY_ID + " = '" + id +
                "' " + " AND " + KEY_NAME + " = '" + word + "'";
        database.execSQL(q);
    }


    public Cursor getId(String word){                           //to get id of phrase
        SQLiteDatabase database = this.getWritableDatabase();
        String query = " SELECT " + KEY_ID +" FROM "+ TABLE_NAME + " WHERE " +KEY_NAME + " = '"+word+"'";
        Cursor cursor = database.rawQuery(query,null);
        return cursor;
    }

    public void deleteData(){                                          //to delete data in table 3
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query="DELETE FROM " +TABLE_NAME3;
        sqLiteDatabase.execSQL(query);

    }


    public  Cursor getCode(){                                           //to get language code
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query="SELECT * FROM " +TABLE_NAME2;
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        return cursor;

    }

    }



