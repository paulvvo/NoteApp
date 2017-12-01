package com.example.sleepy.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    EditText notesEditText;
    Intent getIntent;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getIntent = getIntent();

        notesEditText = (EditText) findViewById(R.id.notesEditText);
        notesEditText.setText(MainActivity.notesList.get(getIntent.getIntExtra("i", Integer.MAX_VALUE)));

        sharedPreferences = this.getSharedPreferences("com.example.sleepy.noteapp", Context.MODE_PRIVATE);


        notesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notesList.remove(getIntent.getIntExtra("i", Integer.MAX_VALUE));
                MainActivity.notesList.add(getIntent.getIntExtra("i", Integer.MAX_VALUE),notesEditText.getText().toString());
                MainActivity.arrayAdapter.notifyDataSetChanged();

                try {
                    sharedPreferences.edit().putString("notesList", ObjectSerializer.serialize(MainActivity.notesList)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    /*
    This part of the code was saving the notes only when the back button was used. The method
    implemented above is better because the text is saved everytime time there is a change to the
    EditText view.
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//
//            MainActivity.notesList.remove(getIntent.getIntExtra("i", Integer.MAX_VALUE));
//            MainActivity.notesList.add(getIntent.getIntExtra("i", Integer.MAX_VALUE),notesEditText.getText().toString());
//            MainActivity.arrayAdapter.notifyDataSetChanged();
//
//            try {
//                sharedPreferences.edit().putString("notesList", ObjectSerializer.serialize(MainActivity.notesList)).apply();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
