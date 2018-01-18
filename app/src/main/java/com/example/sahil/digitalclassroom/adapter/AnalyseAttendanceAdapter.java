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
    //make list of user with percentage this is extended user model
    private ArrayList<User> Members_of_group;
    private int[] percentage_array;

    public AnalyseAttendanceAdapter(ArrayList<User> Members_of_group,int[] present_array,int[] total_array){
        this.Members_of_group = Members_of_group;
        //To calculate the % on the basis of present and total
        int[] percentage_array = new int[present_array.length];
        for (int i=0;i<present_array.length;i++){
            percentage_array[i] = present_array[i]*100/total_array[i];
        }
        this.percentage_array =percentage_array;
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
        Picasso.with(holder.StudentImage.getContext())
                .load(user.getPathImage())//Set the path of the image
                .into(holder.StudentImage);

        holder.StudentPercentage.setText(String.valueOf(percentage_array[position])+"%");
        //For color text in attendance
        if (percentage_array[position]<75){
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
