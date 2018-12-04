package com.example.calixta.doanddone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText xml_edit_text;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //get edit text from lay
        xml_edit_text = (EditText) findViewById(R.id.edit_task);
        //get text from the intent
        xml_edit_text.setText(getIntent().getStringExtra("itemText"));
        position = getIntent().getIntExtra("itemPosition", 0);
        getSupportActionBar().setTitle("Edit an item...");

    }
    //add functionality to the save button
    public void onSaveItem(View v) {
        Intent intent = new Intent();
        intent.putExtra("itemText", xml_edit_text.getText().toString());
        intent.putExtra("itemPosition", position);
        setResult(RESULT_OK, intent);
        finish();

    }
}
