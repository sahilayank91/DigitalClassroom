package com.example.sahil.digitalclassroom.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.TakeAttendenceAdapter;
import com.example.sahil.digitalclassroom.model.User;

import java.util.ArrayList;
import java.util.List;

public class TakeAttendence extends AppCompatActivity {
    private ArrayList<User> Members;
    private RecyclerView recyclerView;
    private TakeAttendenceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        recyclerView = (RecyclerView) findViewById(R.id.list_attendance);

        mAdapter = new TakeAttendenceAdapter(Members);//Provide Members List
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}


