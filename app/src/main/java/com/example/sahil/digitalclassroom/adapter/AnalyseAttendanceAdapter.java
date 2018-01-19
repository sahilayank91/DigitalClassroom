package com.example.sahil.digitalclassroom.adapter;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AnalyseAttendanceAdapter extends RecyclerView.Adapter<AnalyseAttendanceAdapter.MyViewHolder>{
    //make list of user with percentage
    private ArrayList<User> Members_of_group;

    public AnalyseAttendanceAdapter(ArrayList<User> Members_of_group){
        this.Members_of_group = Members_of_group;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView StudentName,StudentId;
        ImageView StudentImage;
        TextView StudentPercentage;
        public MyViewHolder(View view) {
            super(view);
            StudentName = (TextView) view.findViewById(R.id.list_student_name);
            StudentId = (TextView) view.findViewById(R.id.list_student_id);
            StudentImage = (ImageView) view.findViewById(R.id.list_student_avatar);
            StudentPercentage = (CheckBox) view.findViewById(R.id.text_percentage);
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
        Picasso.with(holder.StudentImage.getContext())
                .load(user.getPathImage())//Set the path of the image
                .into(holder.StudentImage);

//        holder.StudentPercentage.setText(String.valueof(user.percentage)+"%");
        //For color text in attendance
        if (90<75){
            holder.StudentPercentage.setTextColor(Color.parseColor("#C62828"));
        }
        else{
            holder.StudentPercentage.setTextColor(Color.parseColor("#2E7D32"));
        }
    }

    @Override
    public int getItemCount() {
        return Members_of_group.size();
    }

}
