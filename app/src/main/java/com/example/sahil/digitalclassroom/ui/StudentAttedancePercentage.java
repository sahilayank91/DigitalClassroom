package com.example.sahil.digitalclassroom.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sahil.digitalclassroom.R;
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

import java.util.ArrayList;

public class StudentAttedancePercentage extends AppCompatActivity {
    public String Group_id;
    public String User_id;
    private ArrayList<User> MembersofGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attedance_percentage);
        Group_id = "-L34ztQ9YP2avt0NyxDU";
        User_id = "id1";

        MembersofGroup = new ArrayList<>();
        findAllMembersinGroup();
    }

    public void executeFirebase() {
        final int[] res = {0,0,0};
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("attendance").orderByChild("user_id").equalTo(User_id);
        final int[] flag = {0};
        //This code is to determine whether the Data is present in the table or not
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    flag[0] = 1;
                    res[0] = 0;
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
                    progressDialog.dismiss();
                    Attendance attendance = dataSnapshot.getValue(Attendance.class);
                    if (attendance.getGroup_id().contains(Group_id)){
                        if (attendance.isIs_present()){
                            res[1]++;
                        }
                        res[2]++;
                        Log.e("show percentage", String.valueOf(res[1]*100/res[2]));
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
                    Log.e("dffff","data is null");
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
                                MembersofGroup.add(user);
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
