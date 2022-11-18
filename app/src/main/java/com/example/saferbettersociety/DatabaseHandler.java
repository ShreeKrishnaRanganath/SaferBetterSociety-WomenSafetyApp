package com.example.saferbettersociety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class DatabaseHandler extends SQLiteOpenHelper {


    public static final String DATABASE ="myList.db";
    public static final String TABLE = "myList_data";
    public static final String COL1= "ID";
    public static final String COL2 = "ITEM1";
    public DatabaseHandler(Context context){super(context, DATABASE , null, 1);}


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String  createTable = "CREATE TABLE " + TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+" ITEM1 TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String a = "DROP TABLE IF EXISTS "+ TABLE;
        sqLiteDatabase.execSQL(a);
        onCreate(sqLiteDatabase);
    }





    public Boolean addData (String item1){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,item1);


        long result = db.insert(TABLE, null,contentValues);
        if (result==-1){
            return false;
        }
        else{
            return true;
        }
    }



    public Cursor getListContents(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE,null);
        return data;
    }
}
