package com.example.sahil.digitalclassroom.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.TakeAttendanceAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        //Get the members list,Groupid,Teacher id in intent

        Group_id = getIntent().getStringExtra("group_id");
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Teacher_id = sharedPreferences.getString("userId","");
        //todo stop student to open this activity
        //todo check if attendance is already submitted
//        Group_id = "-L35-5etjW88CxX5fQ-J";
//        Teacher_id = "-L34I6uEd2FljjwWSI5G";

        Members = new ArrayList<> ();//for populating sample data
        findAllMembersinGroup();//populated the members list
        // aListModel = (ArrayList<Model>) getIntent().getSerializableExtra(KEY); //model class must implement serializable

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
                finish();
            }
        });
    }
//Populate sample data
    public ArrayList<User> PopulateSample(){
        ArrayList<User> Members = new ArrayList<>();
//        User user = new User("hello@gmail.com","hel1","00","ph","otp","department","id1","collegeid",2,"helo");
//        Members.add(user);
//        user = new User("2nd user @gmail.com","hel2","00","ph","otp","department","id2","collegeid",2,"helo");
//        Members.add(user);
        return Members;
    }

    public void createAttendance(ArrayList<User> Members){
        Attendance submitAttendance;
        submitAttendance = new Attendance();
        submitAttendance.setGroup_id(Group_id);
        submitAttendance.setTaken_by(Teacher_id);
        //To get current date and time of system
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        submitAttendance.setDate(formattedDate);

        submitAttendance.setGroupid_date(submitAttendance.getGroup_id()+"_"+submitAttendance.getDate());

        for (int i=0; i<Members.size();i++){
            submitAttendance.setUser_id(Members.get(i).get_id());
            submitAttendance.setIs_present(present_array[i]);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("/attendance");
            DatabaseReference newref = myRef.push();
            newref.setValue(submitAttendance);
        }
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
                    Toast.makeText(TakeAttendance.this,"Please add members in the group, Share the group code with students",Toast.LENGTH_LONG).show();
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
                                Members.add(user);
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
}


