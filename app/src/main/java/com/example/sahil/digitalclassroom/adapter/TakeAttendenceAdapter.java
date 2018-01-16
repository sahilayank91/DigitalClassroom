package com.example.sahil.digitalclassroom.adapter;

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
import java.util.List;


public class TakeAttendenceAdapter extends RecyclerView.Adapter<TakeAttendenceAdapter.MyViewHolder>{

    private List<User> Members_of_group;

    public TakeAttendenceAdapter(ArrayList<User> Members_of_group){
        this.Members_of_group = Members_of_group;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView StudentName,StudentId;
        ImageView StudentImage;
        CheckBox Ispresent;
        public MyViewHolder(View view) {
            super(view);
            StudentName = (TextView) view.findViewById(R.id.list_student_name);
            StudentId = (TextView) view.findViewById(R.id.list_student_id);
            StudentImage = (ImageView) view.findViewById(R.id.list_student_avatar);
            Ispresent = (CheckBox) view.findViewById(R.id.list_is_present);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_take_attendence, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = Members_of_group.get(position);
        holder.StudentName.setText(user.getName());
        holder.StudentId.setText(user.getCollege_id());
        Picasso.with(holder.StudentImage.getContext())
                .load(user.getPathImage())//Set the path of the image
                .into(holder.StudentImage);
//To set the click listener on the list
        holder.Ispresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                ((CheckBox) v).setChecked(!checked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Members_of_group.size();
    }

}
