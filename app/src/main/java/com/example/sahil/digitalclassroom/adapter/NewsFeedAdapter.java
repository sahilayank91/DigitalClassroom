package com.example.sahil.digitalclassroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Post;
import com.example.sahil.digitalclassroom.ui.CommentDetailsActivity;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by sahil on 1/17/2018.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder> {

    private List<Post> postList;
    public Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author,title,date,description, year,comments;
        public ImageView post_image;
        public LinearLayout comment_holder, share_holder;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.newsfeed_title);
            author = (TextView) view.findViewById(R.id.newsfeed_author_name);
            date = (TextView) view.findViewById(R.id.newsfeed_title);
            description = (TextView) view.findViewById(R.id.newsfeed_description);
            year = (TextView) view.findViewById(R.id.newsfeed_date);
            comments = (TextView) view.findViewById(R.id.newsfeed_total_comments);
            post_image = (ImageView) view.findViewById(R.id.newsfeed_image);
            comment_holder = (LinearLayout)view.findViewById(R.id.news_feed_comment_icon_holder);
            share_holder = (LinearLayout)view.findViewById(R.id.news_feed_share_icon_holder);
        }
    }

    public NewsFeedAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_newsfeed, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Post post = postList.get(position);
        holder.author.setText(post.getUsername());
        holder.title.setText(post.getDepartment());
        holder.description.setText(post.getText());
        holder.year.setText(DateUtils.getRelativeTimeSpanString(post.getTime(), new java.util.Date().getTime(), DateUtils.FORMAT_NUMERIC_DATE));
        holder.date.setText(DateUtils.getRelativeTimeSpanString(post.getTime(), new java.util.Date().getTime(), DateUtils.FORMAT_ABBREV_RELATIVE));
        Glide.with(context).load(post.getImage_url()).into(holder.post_image);
        holder.share_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Check this Post titled: " + post.getText());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
        holder.comment_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentDetailsActivity.class);
                intent.putExtra("post_id",post.getPost_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


}
