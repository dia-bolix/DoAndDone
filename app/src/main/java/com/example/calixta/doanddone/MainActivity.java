package com.example.calixta.doanddone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //id activity
    public final static int EDIT_REQUEST_CODE = 100;
    public final static String TEXT = "itemText";
    public  final static  String ITEM_AT_POSITION = "itemPosition";
    //where my tasks will be stored
    ArrayList<String> task_list;
    ArrayAdapter<String> task_list_adapter;
    ListView xml_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //list of items
        readItems();
        task_list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, task_list);
        //connect to list view in xml file
        xml_list = (ListView) findViewById(R.id.tasks);
        xml_list.setAdapter(task_list_adapter);

        //just add some fake ass data
        //task_list.add("Groceries");
        //task_list.add("Homework");
        removeItem();

    }
    //function that adds items
    public void  onAddItem(View v) {
        EditText add_text = (EditText) findViewById(R.id.input);
        //string currently in edit text
        String user_input = add_text.getText().toString();
        //now add the text to the list
        task_list_adapter.add(user_input);
        add_text.setText("");
        write();
        //show a toast to confirm action was completed successfully
        Toast.makeText(getApplicationContext(), "Do!", Toast.LENGTH_SHORT).show();
    }

    private void removeItem() {
        xml_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //remove item from position in list
                task_list.remove(position);
                task_list_adapter.notifyDataSetChanged();
                write();
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //now to edit,,,,
        xml_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create new activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                //pass data to edit text
                intent.putExtra(TEXT, task_list.get(position));
                intent.putExtra(ITEM_AT_POSITION, position);

                //now get back and display
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
           String updatedItem = data.getExtras().getString(TEXT);
           int position = data.getExtras().getInt(ITEM_AT_POSITION);
           task_list.set(position, updatedItem);
           task_list_adapter.notifyDataSetChanged();
           write();
           Toast.makeText(this, "Task edited successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //make sure tasks are saved

    //get the file where it is stored
    private File getdataFile() {
        return new File(getFilesDir(), "todo.txt");

    }
    //get the data already inside the txt
    private  void readItems() {
        try {
            task_list = new ArrayList<>(FileUtils.readLines(getdataFile(), Charset.defaultCharset()));
        } catch (IOException e) { //if nothing saved, make a new list
            Log.e("MainActivity", "Error reading file", e);
            task_list = new ArrayList<>();
        }
    }
    //add items in list to text file that is saved
    private void write() {
        try {
            FileUtils.writeLines(getdataFile(), task_list);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }

    }

