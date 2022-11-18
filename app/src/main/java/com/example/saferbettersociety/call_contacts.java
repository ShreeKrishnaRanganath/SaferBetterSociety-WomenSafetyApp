package com.example.saferbettersociety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class call_contacts extends AppCompatActivity {
    ListView listView;
    DatabaseHandler databaseHandler;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_contacts);


        listView = findViewById(R.id.listView);
        databaseHandler = new DatabaseHandler(this);





        ArrayList<String> theList = new ArrayList<>();
        Cursor data = databaseHandler.getListContents();
        if (data.getCount()==0){

            Toast.makeText(call_contacts.this, "THERE IS NO CONTENT", Toast.LENGTH_SHORT).show();


        }
        else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedFromList = (String) (listView.getItemAtPosition(position));
                        calluser(selectedFromList);
                    }


                });


                }

        }
    }

    private void calluser(String userphone) {
        if (ContextCompat.checkSelfPermission(call_contacts.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(call_contacts.this,
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            return;

        }else {
            String dial = "tel:" + userphone;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (REQUEST_CALL){
            case REQUEST_CALL:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
return;
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

