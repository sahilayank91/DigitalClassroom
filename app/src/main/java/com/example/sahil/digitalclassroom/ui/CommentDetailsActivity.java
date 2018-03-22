package com.example.sahil.digitalclassroom.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.CommentsAdapter;
import com.example.sahil.digitalclassroom.adapter.GroupAdapter;
import com.example.sahil.digitalclassroom.model.Comment;
import com.example.sahil.digitalclassroom.model.Group;
import com.example.sahil.digitalclassroom.model.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class CommentDetailsActivity extends AppCompatActivity {
    private String post_id;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    private List<Comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    private EditText message;
    private ImageView publish_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        post_id = getIntent().getStringExtra("post_id");

        publish_icon =(ImageView)findViewById(R.id.message_publish_icon);
        message = (EditText)findViewById(R.id.message_edit_text);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CommentsAdapter(commentList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareCommentData();

        publish_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushComment();
                message.setText("");
            }
        });
    }



    void pushComment(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    /*User Reference*/
        DatabaseReference ref = database.getReference("/comments");

        DatabaseReference newCommentRef = ref.push();

        String comment_id = newCommentRef.getKey();

        String username = sharedPreferences.getString("username",null);
        String profile_url = sharedPreferences.getString("profile_url",null);
        long time  = System.currentTimeMillis();
        Comment comment = new Comment(comment_id,username,message.getText().toString(),time,profile_url,post_id);

                                    /*Setting the user value in the database*/
        newCommentRef.setValue(comment);





    }

    void prepareCommentData(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

                /*User Reference*/
        DatabaseReference ref = database.getReference().child("comments");

                /*Check if the group id is present or not*/
        String userid = sharedPreferences.getString("userId",null);

        Query query = ref.orderByChild("post_id").equalTo(post_id);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("POST:",dataSnapshot.toString());
                Comment comment = dataSnapshot.getValue(Comment.class);
                commentList.add(comment);
                mAdapter.notifyDataSetChanged();


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
