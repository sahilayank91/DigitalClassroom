package com.example.sahil.digitalclassroom.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.AnalyseAttendanceAdapter;
import com.example.sahil.digitalclassroom.adapter.TakeAttendanceAdapter;
import com.example.sahil.digitalclassroom.model.Attendance;
import com.example.sahil.digitalclassroom.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AnalyseAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private Button fromDate,toDate;
    public long fromDateData,toDateData;
    public int flag;
    private ArrayList<Attendance> listOfatt;
    private ArrayList<User> MembersOfGroup;//Get from the previous activity
    private RecyclerView recyclerView;
    private AnalyseAttendanceAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_attendance);
        fromDate = (Button) findViewById(R.id.button_from_date);
        toDate = (Button) findViewById(R.id.button_to_date);
        //Below functions to pick the date accordingly

        toDateData = System.currentTimeMillis();
        fromDateData = System.currentTimeMillis();

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                DialogFragment datePicker = new DatePickerFragmentFrom();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                DialogFragment datePicker = new DatePickerFragmentTo();
                datePicker.show(getSupportFragmentManager(),"date picker");
                Log.e("data","inside click listener");

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Query query = reference.child("attendance").orderByChild("date").startAt(fromDateData).endAt(toDateData);
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Attendance attendance = dataSnapshot.getValue(Attendance.class);
//                        Log.e("reading", String.valueOf(fromDateData)+String.valueOf(toDateData));
                        if (attendance != null && attendance.getGroup_id().contains("iiitk cse 3rd year english") ) {
                            listOfatt.add(attendance);
                            Log.e("reading", "list in given date range"+listOfatt.get(listOfatt.size() - 1).getUser_id());
                            refreshRecyclerView();
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
                });
//                Log.e("data","at last listener");

            }
        });

        listOfatt = new ArrayList<>();
        MembersOfGroup = PopulateSample();


        recyclerView = (RecyclerView) findViewById(R.id.list_analyse_attendance);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        refreshRecyclerView();


    }

    public ArrayList<User> PopulateSample(){
        ArrayList<User> Members = new ArrayList<>();
        User user = new User("hello@gmail.com","hel1","00","ph","otp","department","id1","collegeid",2,"helo");
        Members.add(user);
        user = new User("2nd user @gmail.com","hel2","00","ph","otp","department","id2","collegeid",2,"helo");
        Members.add(user);
        return Members;
    }

    public void refreshRecyclerView(){
        //        MembersOfGroup will have all the members of group
        //todo pass list of members to the adapter with % of attendance
        //Try to use join
        //This function should be invoked after list is updated
        Log.e("recyclerview","inside recycle view updater");
//        int[] present_array = new int[MembersOfGroup.size()];
//        int[] total_array = new int[MembersOfGroup.size()];

        int[] present_array = {4,3};
        int[] total_array = {5,7};
        int i = 0;
        //To calculate the present and total array
//        for (User x:MembersOfGroup) {
//            for (Attendance y: listOfatt){
//                if (y.getUser_id() == x.get_id()){
//                    total_array[i]++;
//                    if (y.isIs_present()){
//                        present_array[i]++;
//                    }
//                }
//            }
//            i+=1;
//        }

        Log.e("recyclerview", String.valueOf(total_array[0]));
        Log.e("recyclerview", String.valueOf(present_array[0]));

        if (total_array[0]!= 0) {
            mAdapter = new AnalyseAttendanceAdapter(MembersOfGroup, present_array, total_array);//Provide Members List
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }

    }
    //To set the date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        if (flag==1) {
            String From_button_text = "From : " + DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime());
            fromDate.setText(From_button_text);
            fromDateData = calendar.getTimeInMillis();//Get the date time int the desired format

        }
        else {
            String From_button_text = "To : " + DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime());
            toDate.setText(From_button_text);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth+1);
            toDateData = calendar.getTimeInMillis();//Get the date time int the desired format
        }
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
            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
        }
    }

    public static class DatePickerFragmentFrom extends DialogFragment {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,0,1);
        }
    }
}

