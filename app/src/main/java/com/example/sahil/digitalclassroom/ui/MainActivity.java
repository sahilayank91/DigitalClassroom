package com.example.sahil.digitalclassroom.ui;

import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.GroupAdapter;
import com.example.sahil.digitalclassroom.model.Group;
import com.example.sahil.digitalclassroom.model.User_Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    private TextView mTextMessage;
    private Button signout;
    private FirebaseUser user;
    private Button createPost;
    private String selectedLanguage;

    private List<Group> groupList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GroupAdapter mAdapter;
    private String group_name;
    private String group_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this,TakeAttendance.class);
//        intent.putExtra("group_id","-L34ztQ9YP2avt0NyxDU");
//        startActivity(intent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        String userId = sharedPreferences.getString("userId",null);
        Log.e("userId: ",userId);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new GroupAdapter(groupList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareGroupData();



    }

    void prepareGroupData(){



        final int[] flag = {0};
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Groups");
        progressDialog.setMessage("Loading the Groups..Please wait ");
        progressDialog.show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

                /*User Reference*/
        DatabaseReference ref = database.getReference("/user_group");

                /*Check if the group id is present or not*/
        String userid = sharedPreferences.getString("userId",null);
//        userid = "-L3Gwdsus-eY6pTtV5B_";

        Log.e("userId in prepare: ",userid+"");
        Query query = ref.orderByChild("user_id").equalTo(userid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"You are not listed in any Group. Please Create/Join group.",Toast.LENGTH_LONG).show();
                    flag[0] = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        if(flag[0]== 0) {
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.e("dta snap: ", "The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());


                    User_Group user_group = dataSnapshot.getValue(User_Group.class);
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                /*User Reference*/
                    DatabaseReference ref = database.getReference("/group");

                /*Check if the group id is present or not*/
                    Query query = ref.orderByChild("group_id").equalTo(user_group.getGroup_id());

                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Group group = dataSnapshot.getValue(Group.class);
                            groupList.add(group);
                            mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
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

    public void joinGroup(){
       AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Join Group");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                group_code = input.getText().toString();


                /*Simulating Database access to create Group*/
                /*Getting firebase Database configuration*/
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                /*User Reference*/
                DatabaseReference ref = database.getReference("/group");

                /*Check if the group id is present or not*/
                Query query = ref.orderByChild("group_join_code").equalTo(group_code);

                query.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e("data", String.valueOf(dataSnapshot.getValue(Group.class)));

                        Group group = dataSnapshot.getValue(Group.class);

                        DatabaseReference reference = database.getReference("/user_group");
                        DatabaseReference newUserRef = reference.push();

                           /*Get group id*/
                        String groupId = group.getGroup_id();
                        String userId = sharedPreferences.getString("userId",null);
                        Log.e("userid:",userId);
                        long time = System.currentTimeMillis();

                        Log.e("groupId:",group.getGroup_id());
                        Log.e("user:",userId);

                        String user_group_id = newUserRef.getKey();
                        User_Group user_group  = new User_Group(userId,groupId,user_group_id,time);

                        newUserRef.setValue(user_group);
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
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();
    }

    public void createGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Create Group");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                group_name = input.getText().toString();

                String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String Small_chars = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";



                String values = Capital_chars + Small_chars +
                        numbers ;

                // Using random method
                Random rndm_method = new Random();

                char[] password = new char[6];

                for (int i = 0; i < 6; i++)
                {
                    // Use of charAt() method : to get character value
                    // Use of nextInt() as it is scanning the value as int
                    password[i] =
                            values.charAt(rndm_method.nextInt(values.length()));

                }
                String group_code = password.toString();
                /*Simulating Database access to create Group*/
                /*Getting firebase Database configuration*/
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                /*User Reference*/
                DatabaseReference ref = database.getReference("/group");
                DatabaseReference newGroupRef = ref.push();
                String GroupId = newGroupRef.getKey();
                String teacher_id = sharedPreferences.getString("userId",null);
                Group group = new Group(GroupId,teacher_id,group_name,group_code);
                /*Setting the user value in the database*/
                newGroupRef.setValue(group);








                /*Adding to User Group Schema too*/
                DatabaseReference reference = database.getReference("/user_group");
                DatabaseReference newUserRef = reference.push();

                String userId = sharedPreferences.getString("userId",null);
                long time = System.currentTimeMillis();
                String user_group_id = newUserRef.getKey();
                User_Group user_group  = new User_Group(userId,GroupId,user_group_id,time);
                newUserRef.setValue(user_group);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.navigation_home) {
          FirebaseAuth.getInstance().signOut();
          return true;
        }if(id == R.id.navigation_group){
            int role = sharedPreferences.getInt("role",0);
            Log.e("role: ",String.valueOf(role));
            if(role == 1){
                createGroup();
            }else{
                joinGroup();
            }
        }if(id==R.id.navigation_post){

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_share) {
            //
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }else if(id == R.id.nav_create_group){
            int role = sharedPreferences.getInt("role",0);

            if(role==1){
                createGroup();
            }else{
                joinGroup();
            }

        }else if(id == R.id.nav_attendance){


        }else if(id == R.id.nav_assignment){

        }else if(id == R.id.nav_profile){
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




}
