package com.example.sahil.digitalclassroom.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;




public class TakeAttendanceAdapter extends RecyclerView.Adapter<TakeAttendanceAdapter.MyViewHolder>{

//    public  List<Boolean> ispresent_array;
    static public Boolean[] present_array;
    private ArrayList<User> Members_of_group;

    public TakeAttendanceAdapter(ArrayList<User> Members_of_group){
        this.Members_of_group = Members_of_group;
//        List<Boolean> list=new ArrayList<Boolean>(Arrays.asList(new Boolean[Members_of_group.size()]));
//        Collections.fill(list, Boolean.TRUE);
        present_array = new Boolean[Members_of_group.size()];
        for(int i=0;i<Members_of_group.size();i++){
            present_array[i] = false;
        }
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
                .inflate(R.layout.item_list_take_attendance, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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
                ((CheckBox) v).setChecked(checked);
//                Boolean a = ispresent_array.get(position);
                present_array[position] = checked;
                Log.e("assign", String.valueOf(present_array[position]));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Members_of_group.size();
    }

}
