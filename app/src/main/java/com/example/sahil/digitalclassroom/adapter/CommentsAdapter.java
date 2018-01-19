package com.example.sahil.digitalclassroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Comment;
import com.example.sahil.digitalclassroom.model.Group;
import com.example.sahil.digitalclassroom.ui.DashBoardActivity;
import com.example.sahil.digitalclassroom.ui.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by sahil on 1/17/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private List<Comment> commentList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author, time, text;
        public ImageView profile_image;
        public EditText message;
        public Button send;


        public MyViewHolder(View view) {
            super(view);

                author = (TextView) view.findViewById(R.id.comment_publisher);
                time= (TextView)view.findViewById(R.id.comment_date_time);
                text =(TextView)view.findViewById(R.id.comment_text);
                profile_image = (ImageView)view.findViewById(R.id.comment_publisher_image);
                message = (EditText)view.findViewById(R.id.message_edit_text);
                send = (Button)view.findViewById(R.id.message_publish_icon);
        }
    }

    public CommentsAdapter(List<Comment> commentList, Context context)
    {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.author.setText(comment.getAuthor());
        holder.time.setText(DateUtils.getRelativeTimeSpanString(comment.getTime(), new java.util.Date().getTime(), DateUtils.FORMAT_ABBREV_RELATIVE));
        holder.text.setText(comment.getText());
        Glide.with(context).load(comment.getProfile_url()).into(holder.profile_image);
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = holder.text.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    /*User Reference*/
                DatabaseReference ref = database.getReference("/post_comments");

                DatabaseReference newUserRef = ref.push();











            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


}
