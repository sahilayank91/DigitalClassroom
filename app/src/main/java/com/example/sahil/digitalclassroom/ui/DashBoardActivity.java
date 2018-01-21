package com.example.sahil.digitalclassroom.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.NewsFeedAdapter;
import com.example.sahil.digitalclassroom.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsFeedAdapter mAdapter;
    private String group_id;
    private Button uploadPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        group_id = getIntent().getStringExtra("group_id");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(DashBoardActivity.this,CreatepostActivity.class);
                intent.putExtra("group_id",group_id);
                startActivity(intent);
                finish();
            }
        });



        /*Enabling create post button*/


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new NewsFeedAdapter(postList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int role = sharedPreferences.getInt("role",0);

        if(role == 0){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_take_attendance).setVisible(false);
            nav_Menu.findItem(R.id.nav_assignment_create).setVisible(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        preparePostData();



    }



    public void preparePostData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Groups");
        progressDialog.setMessage("Loading the Groups..Please wait ");
        progressDialog.show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

                /*User Reference*/
        DatabaseReference ref = database.getReference().child("posts");

                /*Check if the group id is present or not*/
        String userid = sharedPreferences.getString("userId", null);

        Query query = ref.orderByChild("group_id").equalTo(group_id);

        final int[] flag = {0};

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(DashBoardActivity.this, "No Posts have been Created for this Group. Please Click + sign to create a Post.", Toast.LENGTH_LONG).show();
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
                    Log.e("POST:", dataSnapshot.toString());
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
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
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Check the new App for the Digital Classroom");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            //
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.nav_take_attendance){
            Intent intent = new Intent(DashBoardActivity.this, TakeAttendance.class);
            intent.putExtra("group_id",group_id);
            startActivity(intent);
            finish();

        }else if(id == R.id.nav_analyse_attendance){
            Intent intent = new Intent (DashBoardActivity.this,AnalyseAttendance.class);
            intent.putExtra("group_id",group_id);
            startActivity(intent);

        }
        else if(id == R.id.nav_assignment){
            Intent intent =new Intent(DashBoardActivity.this,AssignmentActivity.class);
            intent.putExtra("group_id",group_id);

            startActivity(intent);

        }else if(id == R.id.nav_assignment_create){
            Intent intent =new Intent(DashBoardActivity.this,AssignmentCreateActivity.class);
            intent.putExtra("group_id",group_id);

            startActivity(intent);

        }else if(id == R.id.nav_profile){
            Intent intent = new Intent(DashBoardActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_create_post){
            Intent intent = new Intent(DashBoardActivity.this,CreatepostActivity.class);
            intent.putExtra("group_id",group_id);
            startActivity(intent);
            finish();
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
