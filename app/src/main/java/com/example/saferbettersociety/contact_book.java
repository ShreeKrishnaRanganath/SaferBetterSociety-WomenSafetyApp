package com.example.saferbettersociety;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class contact_book extends AppCompatActivity {
    ImageButton button1, button2, button3, button4;
    EditText edittext;
    TextView nocon;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_book);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button4 = findViewById(R.id.button4);
        edittext = findViewById(R.id.edittext);
        listView = findViewById(R.id.listview);
        nocon = findViewById(R.id.tvnocon);
        databaseHandler = new DatabaseHandler(this);
        loadData();
        button2.setEnabled(false);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sav = edittext.getText().toString();
                if (sav.length()==10) {
                    boolean insertdata = databaseHandler.addData(sav);
                    if (insertdata == true) {
                        Toast.makeText(contact_book.this, "DATA ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contact_book.this, "DATA FAILED TO SAVE", Toast.LENGTH_SHORT).show();
                    }
                    edittext.setText("");
                    loadData();
                }
                else
                {
                    Toast.makeText(contact_book.this, "Please enter a 10 digit Phone Number", Toast.LENGTH_SHORT).show();
                }

            }
        });



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittext.setText("");
                Toast.makeText(contact_book.this, "DATA CLEAR SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                button2.setEnabled(true);
                String selectedFromList = (String) (listView.getItemAtPosition(position));
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqLiteDatabase = databaseHandler.getWritableDatabase();
                        deleteData(selectedFromList);
                        Toast.makeText(contact_book.this, "DATA CLEARED SUCCESFULLY", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                });


            }});
    }

    private void loadData(){
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = databaseHandler.getListContents();
        if (data.getCount()==0){
            nocon.setText("No Contacts to display");

        }
        else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
                nocon.setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean deleteData(String del){
        return sqLiteDatabase.delete(DatabaseHandler.TABLE ,DatabaseHandler.COL2 + "=?" , new String[]{del})>0 ;

    }




}

