package com.example.sahil.digitalclassroom.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.NewsFeedAdapter;
import com.example.sahil.digitalclassroom.model.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

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
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        group_id = getIntent().getStringExtra("group_id");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new NewsFeedAdapter(postList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        preparePostData();



    }



    public void preparePostData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Groups");
        progressDialog.setMessage("Loading the Groups..Please wait ");
        progressDialog.show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

                /*User Reference*/
        DatabaseReference ref = database.getReference().child("posts");

                /*Check if the group id is present or not*/
        String userid = sharedPreferences.getString("userId",null);

        Query query = ref.orderByChild("group_id").equalTo(group_id);

       query.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Log.e("POST:",dataSnapshot.toString());
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
