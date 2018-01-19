package com.example.sahil.digitalclassroom.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.AssignmentListAdapter;
import com.example.sahil.digitalclassroom.adapter.TakeAttendanceAdapter;
import com.example.sahil.digitalclassroom.model.Assignment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Sumir on 16-01-2018.
 */

public class AssignmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssignmentListAdapter mAdapter;
    private ArrayList<Assignment> AssignmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        AssignmentList = new ArrayList();
        getDataIntoAssignmentList();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_assignment);

        mAdapter = new AssignmentListAdapter(AssignmentList);//Provide Members List
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar_assign);
//        setSupportActionBar(toolbar);
        //if teacher
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.floatingActionButtonAssignment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create new assignment activity
                startActivity(new Intent(AssignmentActivity.this,AssignmentCreateActivity.class));
            }
        });
//        else hide this button

    }

    public ArrayList<Assignment> PopulateSample(){
        ArrayList<Assignment> Members = new ArrayList<>();
        Assignment assignment = new Assignment("dsad","abcd","kkl",System.currentTimeMillis(),0L,20.0,"department");
        Members.add(assignment);
        Members.add(assignment);
        Members.add(assignment);
        Members.add(assignment);
        Assignment assignment2 = new Assignment("dsad","abcd2","kkl",0L,0L,20.0,"department");
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        Members.add(assignment2);
        return Members;
    }

    public void getDataIntoAssignmentList(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("assignments");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Assignment assignment = dataSnapshot.getValue(Assignment.class);
                if(assignment != null)
                {
                    Log.d("hjkk","dasd");
                    AssignmentList.add(assignment);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
