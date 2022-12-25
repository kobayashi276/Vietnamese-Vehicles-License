package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvPerson;
    ArrayList<menu_class> listPerson;
    MenuAdapter adapter;

    //Khởi tạo giao diện
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvPerson = findViewById(R.id.rvPerson);
        listPerson = menu_class.init();
        rvPerson.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuAdapter(this,listPerson);
        rvPerson.setAdapter(adapter);
    }
}