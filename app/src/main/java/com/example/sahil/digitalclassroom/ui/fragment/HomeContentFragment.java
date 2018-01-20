package com.example.sahil.digitalclassroom.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sahil.digitalclassroom.R;

/**
 * Created by Sahil on 6/26/2016.
 */
public class HomeContentFragment extends Fragment {

    private TextView department;
    private TextView name;
    private TextView Email;
    private TextView membersince;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        department = (TextView)container.findViewById(R.id.profile_department_name);
        name = (TextView)container.findViewById(R.id.profile_name);
        Email = (TextView)container.findViewById(R.id.profile_email);
        membersince = (TextView)container.findViewById(R.id.profile_member_since);


        return inflater.inflate(R.layout.activity_home_fragment, null);
    }



}
