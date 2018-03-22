package com.example.sahil.digitalclassroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Group;
import com.example.sahil.digitalclassroom.ui.DashBoardActivity;

import java.util.List;

/**
 * Created by sahil on 1/17/2018.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private List<Group> groupList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public RelativeLayout group_tile;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.tile_title);
            group_tile = (RelativeLayout) view.findViewById(R.id.group_tile);
        }
    }

    public GroupAdapter(List<Group> groupList, Context context)
    {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tile, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Group group = groupList.get(position);
        holder.title.setText(group.getName());
        holder.group_tile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DashBoardActivity.class);
                intent.putExtra("group_id",group.getGroup_id());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


}
