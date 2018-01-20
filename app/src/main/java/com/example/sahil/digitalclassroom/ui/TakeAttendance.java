package com.example.sahil.digitalclassroom.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.TakeAttendanceAdapter;
import com.example.sahil.digitalclassroom.model.Attendence;
import com.example.sahil.digitalclassroom.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.sahil.digitalclassroom.adapter.TakeAttendanceAdapter.present_array;

public class TakeAttendance extends AppCompatActivity {
    private ArrayList<User> Members;
    public  List<Boolean> ispresent_array;
    private RecyclerView recyclerView;
    private TakeAttendanceAdapter mAdapter;
    private Button submitButton;
    private Attendence submitAttendence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        //Members = PopulateSample();//for populating sample data
        submitButton = (Button) findViewById(R.id.submit_attendance);

        recyclerView = (RecyclerView) findViewById(R.id.list_attendance);



        mAdapter = new TakeAttendanceAdapter(Members);//Provide Members List
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAttendance(Members);
            }
        });
    }

    public void createAttendance(ArrayList<User> Members){
        submitAttendence = new Attendence();
        submitAttendence.setGroup_id("iiitk cse 3rd year english");
        submitAttendence.setTaken_by("teacher_id");
        long time = System.currentTimeMillis();
        submitAttendence.setDate(time);
        for (int i=0; i<Members.size();i++){
            submitAttendence.setUser_id(Members.get(i).get_id());
            submitAttendence.setIs_present(present_array[i]);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("/attendance");
            DatabaseReference newref = myRef.push();
            newref.setValue(submitAttendence);

        }

    }
}


