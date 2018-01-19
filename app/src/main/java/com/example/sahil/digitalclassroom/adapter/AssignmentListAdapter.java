package com.example.sahil.digitalclassroom.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Assignment;
import com.example.sahil.digitalclassroom.ui.AssignmentActivity;
import com.example.sahil.digitalclassroom.ui.AssignmentCreateActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.sahil.digitalclassroom.ui.AssignmentCreateActivity.isBehindCurrentDate;

/**
 * Created by Sumir on 19-01-2018.
 */

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.MyViewHolder>{
    //make list of user with percentage
    private ArrayList<Assignment> AssignmentList;

    public AssignmentListAdapter(ArrayList<Assignment> AssignmentList){
        this.AssignmentList = AssignmentList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView assignmentName;
        TextView assignmentDueDateText;
        TextView assignmentDueDate;
        TextView assignmentMarks;
        ImageButton assignmentDownload;
        ImageButton assignmentUpload;

        public MyViewHolder(View view) {
            super(view);
            assignmentName = (TextView) view.findViewById(R.id.assignmentListName);
            assignmentDueDateText = (TextView) view.findViewById(R.id.assignmentListDueDateText);
            assignmentDueDate = (TextView) view.findViewById(R.id.assignmentListDueDate);
            assignmentMarks = (TextView) view.findViewById(R.id.assignmentListMarks);
            assignmentDownload = (ImageButton) view.findViewById(R.id.assignmentDownload);
            assignmentUpload = (ImageButton) view.findViewById(R.id.assignmentUpload);
        }
    }

    @Override
    public AssignmentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_assignment, parent, false);

        //        if teacher
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(AssignmentActivity.this, AssignmentSubmittedActivity.class));
            }
        });
        return new AssignmentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignmentListAdapter.MyViewHolder holder, int position) {
        Assignment assignment = AssignmentList.get(position);
        holder.assignmentName.setText(assignment.getName());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(assignment.getDueDate()));
        holder.assignmentDueDate.setText(DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime()));

        if(isBehindCurrentDate(calendar)) {

            holder.assignmentDueDate.setTextColor(Color.parseColor("#C62828"));
        }
        else{
            holder.assignmentDueDate.setTextColor(Color.parseColor("#2E7D32"));
        }

        holder.assignmentMarks.setText(assignment.getMarks() + " Marks");

        //if student then show
        holder.assignmentDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //download respective assignment;
            }
        });

        holder.assignmentUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload an assignment to assignment submit
            }
        });
        //else
//            hide these two buttons

    }

    @Override
    public int getItemCount() {
        return AssignmentList.size();
    }
}
