package com.example.listcity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView cityList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dataList;

    private int selectedPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cityList = findViewById(R.id.city_list);

        String[] cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "Sapporo"};
        dataList = new ArrayList<>(Arrays.asList(cities));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, dataList);

        cityList.setAdapter(adapter);


        cityList.setOnItemClickListener((parent, view, position, id) -> {
            selectedPos = position;
            cityList.setItemChecked(position, true);
        });

        findViewById(R.id.add_button).setOnClickListener(v -> showAddDialog());

        findViewById(R.id.delete_button).setOnClickListener(v -> showDeleteDialog());
    }

    private void showAddDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter a city");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Add City")
                .setView(input)
                .setNegativeButton("CANCEL", (d, which) -> d.dismiss())
                .setPositiveButton("CONFIRM", (d, which) -> {
                    String newCity = input.getText().toString().trim();
                    if (TextUtils.isEmpty(newCity)) {
                        Toast.makeText(this, "City name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dataList.add(newCity);
                    adapter.notifyDataSetChanged();
                    cityList.smoothScrollToPosition(dataList.size() - 1);
                })
                .show();
    }

    private void showDeleteDialog() {
        if (selectedPos < 0 || selectedPos >= dataList.size()) {
            Toast.makeText(this, "Please select a city first", Toast.LENGTH_SHORT).show();
            return;
        }

        String toDelete = dataList.get(selectedPos);

        new AlertDialog.Builder(this)
                .setTitle("Delete City")
                .setMessage("Delete \"" + toDelete + "\"?")
                .setNegativeButton("CANCEL", (d, which) -> d.dismiss())
                .setPositiveButton("CONFIRM", (d, which) -> {
                    dataList.remove(selectedPos);
                    adapter.notifyDataSetChanged();

                    selectedPos = -1;
                    cityList.clearChoices();
                })
                .show();
    }
}
