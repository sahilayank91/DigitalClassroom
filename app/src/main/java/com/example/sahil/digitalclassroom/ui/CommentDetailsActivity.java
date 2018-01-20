package com.example.sahil.digitalclassroom.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.CommentsAdapter;
import com.example.sahil.digitalclassroom.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentDetailsActivity extends AppCompatActivity {
    private String post_id;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    private List<Comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        post_id = getIntent().getStringExtra("post_id");

        mAdapter = new CommentsAdapter(commentList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareCommentData();
    }

    void prepareCommentData(){

    }
}
