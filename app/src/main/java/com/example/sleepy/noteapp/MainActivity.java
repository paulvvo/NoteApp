package com.example.sleepy.noteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notesList;
    static ArrayAdapter arrayAdapter;
    ListView notesListView;
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.newNoteId){
            notesList.add("New Note");
            arrayAdapter.notifyDataSetChanged();
            intent.putExtra("i", notesList.size() -1);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(getApplicationContext(), Main2Activity.class);

        notesList = new ArrayList<String>();


        sharedPreferences = this.getSharedPreferences("com.example.sleepy.noteapp", Context.MODE_PRIVATE);

        try {
            notesList = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("notesList", ObjectSerializer.serialize(new ArrayList<String>())));
            } catch (IOException e) {
            e.printStackTrace();
        }

        if(notesList.size() < 1){
            notesList = new ArrayList<String>(asList("Example Note"));
        }


        notesListView = (ListView) findViewById(R.id.notesListView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notesList);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.putExtra("i", i);
                startActivity(intent);
            }
        });


        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int globalI =i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notesList.remove(globalI);
                                arrayAdapter.notifyDataSetChanged();

                                try {
                                    sharedPreferences.edit().putString("notesList", ObjectSerializer.serialize(notesList)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("NO",null)
                        .show();


                return true;
            }
        });

    }
}
