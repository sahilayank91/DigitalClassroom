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
import com.example.sahil.digitalclassroom.model.Attendance;
import com.example.sahil.digitalclassroom.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.sahil.digitalclassroom.adapter.TakeAttendanceAdapter.present_array;

public class TakeAttendance extends AppCompatActivity {
    private ArrayList<User> Members;
    private RecyclerView recyclerView;
    private TakeAttendanceAdapter mAdapter;
    private Button submitButton;
    //These 2 values are fetched at time of creation of activity
    private String Group_id;
    private String Teacher_id;
    private ArrayList<Attendance> listOfatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        Members = PopulateSample();//for populating sample data
        //todo take the members of group and populate the list Members
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

    public ArrayList<User> PopulateSample(){
        ArrayList<User> Members = new ArrayList<>();
        User user = new User("hello@gmail.com","hel1","00","ph","otp","department","id1","collegeid",2,"helo");
        Members.add(user);
        user = new User("2nd user @gmail.com","hel2","00","ph","otp","department","id2","collegeid",2,"helo");
        Members.add(user);
        return Members;
    }

    public void createAttendance(ArrayList<User> Members){
        Attendance submitAttendance;
        submitAttendance = new Attendance();
        submitAttendance.setGroup_id("iiitk cse 3rd year compiler");
        submitAttendance.setTaken_by("teacher_id");
        long time = System.currentTimeMillis();
        submitAttendance.setDate(time);
        for (int i=0; i<Members.size();i++){
            submitAttendance.setUser_id(Members.get(i).get_id());
            submitAttendance.setIs_present(present_array[i]);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("/attendance");
            DatabaseReference newref = myRef.push();
            newref.setValue(submitAttendance);

        }
    }
}


