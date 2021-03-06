package com.example.sahil.digitalclassroom.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.User;

import java.util.ArrayList;


public class AnalyseAttendanceAdapter extends RecyclerView.Adapter<AnalyseAttendanceAdapter.MyViewHolder>{
    //make list of user with percentage this is extended user model
    private ArrayList<User> Members_of_group;
    static public boolean[] present_array;
    private Context context;


    public AnalyseAttendanceAdapter(ArrayList<User> Members_of_group, Context context){
        this.Members_of_group = Members_of_group;
        this.present_array =present_array;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView StudentName,StudentId;
        ImageView StudentImage;
        TextView StudentPercentage;//From now it will display the status present or absent
        public MyViewHolder(View view) {
            super(view);
            StudentName = (TextView) view.findViewById(R.id.list_student_name);
            StudentId = (TextView) view.findViewById(R.id.list_student_id);
            StudentImage = (ImageView) view.findViewById(R.id.list_student_avatar);
            StudentPercentage = (TextView) view.findViewById(R.id.text_percentage);
        }
    }

    @Override
    public AnalyseAttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_analyse_attendance, parent, false);

        return new AnalyseAttendanceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnalyseAttendanceAdapter.MyViewHolder holder, int position) {
        User user = Members_of_group.get(position);
        holder.StudentName.setText(user.getName());
        holder.StudentId.setText(user.getCollege_id());
//        Picasso.with(holder.StudentImage.getContext())
//                .load(user.getPathImage())//Set the path of the image
//                .into(holder.StudentImage);

        Glide.with(context).load(user.getProfile_url()).into(holder.StudentImage);

        //For color text in attendance
        if (!present_array[position]){
            holder.StudentPercentage.setText("Absent");
            holder.StudentPercentage.setTextColor(Color.parseColor("#C62828"));
        }
        else{
            holder.StudentPercentage.setText("Present");
            holder.StudentPercentage.setTextColor(Color.parseColor("#2E7D32"));
        }
    }

    @Override
    public int getItemCount() {
        return Members_of_group.size();
    }

}
