package com.example.sahil.digitalclassroom.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.AnalyseAttendanceAdapter;
import com.example.sahil.digitalclassroom.model.Attendance;
import com.example.sahil.digitalclassroom.model.User;
import com.example.sahil.digitalclassroom.model.User_Group;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AnalyseAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private Button DateButton;
    public String DateData;
    private ArrayList<Attendance> listOfatt;
    private ArrayList<User> MembersOfGroup;//Get from the previous activity
    private RecyclerView recyclerView;
    private AnalyseAttendanceAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    boolean[] present_array;
    private int role;//0 for student and 1 for teacher
    private String User_id,Group_id;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_attendance);
        DateButton = (Button) findViewById(R.id.button_date_analyse);
        //Below functions to pick the date accordingly
        Calendar c = Calendar.getInstance();
        DateButton.setText("Date : " + DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(c.getTime()));

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Group_id = getIntent().getStringExtra("group_id");
        User_id = sharedPreferences.getString("userId","");
        role = sharedPreferences.getInt("role",0);

        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragmentTo();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

//        Group_id = getIntent().getStringExtra("group_id");
//        User_id = getIntent().getStringExtra("teacher_id");
//        Group_id = "-L34ztQ9YP2avt0NyxDU";

        //Below things are based on teacher role now
        listOfatt = new ArrayList<>();
        //Get member list
        if (role==0) {
            MembersOfGroup = new ArrayList<>();
            present_array = new boolean[1];
            getuser();
        }
        else{
            MembersOfGroup = new ArrayList<>();
            findAllMembersinGroup();
        }

        recyclerView = (RecyclerView) findViewById(R.id.list_analyse_attendance);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAdapter = new AnalyseAttendanceAdapter(MembersOfGroup, present_array);//Provide Members List
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    public void findAllMembersinGroup(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("user_group").orderByChild("group_id").equalTo(Group_id);
        final int[] flag = {0};
        //This code is to determine whether the Data is present in the table or not
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(AnalyseAttendance.this,"Please add members in the group, Share the group code with students",Toast.LENGTH_LONG).show();
                    flag[0] = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (flag[0] == 0) {
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User_Group user_group = dataSnapshot.getValue(User_Group.class);
                    Log.e("user_group",user_group.getUser_id());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query q = ref.child("users/"+user_group.getUser_id());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("User ", dataSnapshot.toString());
                            User user = dataSnapshot.getValue(User.class);
                            if (user!=null) {
                                Log.e("User ", user.get_id());
                                MembersOfGroup.add(user);
                                present_array = new boolean[MembersOfGroup.size()];
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

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
                public void onCancelled(DatabaseError firebaseError) {

                }
            });
        }
    }

    private void getuser(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query q = ref.child("users/"+User_id);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("User ", dataSnapshot.toString());
                User user = dataSnapshot.getValue(User.class);
                if (user!=null) {
                    Log.e("User ", user.get_id());
                    MembersOfGroup.add(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ArrayList<User> PopulateSample() {
        ArrayList<User> Members = new ArrayList<>();
        return Members;
    }

    public void refreshRecyclerView() {
        //        MembersOfGroup will have all the members of group
        int i;
        User x;
        for (i=0;i<MembersOfGroup.size();i++) {
            x = MembersOfGroup.get(i);
            for (Attendance a:listOfatt) {
                if (x.get_id().contains(a.getUser_id())){
                    present_array[i] = a.isIs_present();
                    break;
                }
            }
        }
    }

//Fucntion to query data from firebase
    public void ExecuteFirebase(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        listOfatt = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("attendance").orderByChild("groupid_date").equalTo(Group_id+"_"+DateData);
        final int[] flag = {0};
        //This code is to determine whether the Data is present in the table or not
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0){
                    progressDialog.dismiss();
                    flag[0] = 1;
                    Toast.makeText(getApplicationContext(),"DatanotAvailable",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (flag[0] ==0){
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Attendance attendance = dataSnapshot.getValue(Attendance.class);
                progressDialog.dismiss();
                if (attendance != null ) {
                    listOfatt.add(attendance);
                    refreshRecyclerView();
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
            public void onCancelled(DatabaseError firebaseError) {

            }
        });}
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        DateButton.setText("Date : " + DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime()));
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateData = df.format(calendar.getTime());
        if (role == 1) {
            ExecuteFirebase();
        }
        else{
        ExecuteFirebaseStudent();}
    }

    private void ExecuteFirebaseStudent() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("attendance").orderByChild("groupid_date").equalTo(Group_id+"_"+DateData);
        final int[] flag = {0};
        //This code is to determine whether the Data is present in the table or not
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0){
                    progressDialog.dismiss();
                    flag[0] = 1;
                    Toast.makeText(getApplicationContext(),"DatanotAvailable",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (flag[0] ==0){
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressDialog.dismiss();
                Attendance attendance = dataSnapshot.getValue(Attendance.class);
                User x;
                x = MembersOfGroup.get(0);
                if (attendance != null && attendance.getUser_id().contains(x.get_id()) ) {
                    present_array[0] = attendance.isIs_present();
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
            public void onCancelled(DatabaseError firebaseError) {

            }
        });}
    }

    //Two fragment of date picker for picking date 
    public static class DatePickerFragmentTo extends DialogFragment {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }

}

