package com.example.sahil.digitalclassroom.ui.fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Image;

/**
 * Created by Sahil on 6/26/2016.
 */
public class HomeContentFragment extends Fragment {

    private TextView department;
    private TextView name;
    private TextView Email;
    private TextView membersince;
    private ImageView profile_image;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        department = (TextView)container.findViewById(R.id.profile_department_name);
//        name = (TextView)container.findViewById(R.id.profile_name);
//        Email = (TextView)container.findViewById(R.id.profile_email);
//        membersince = (TextView)container.findViewById(R.id.profile_member_since);
//        profile_image = (ImageView) container.findViewById(R.id.profile_image);
//        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        String names = sharedPreferences.getString("username","name");
//        name.setText(names);
//        String profile_url = sharedPreferences.getString("profile_url",null);
//        String email   =sharedPreferences.getString("email",null);
//        Email.setText(email);
//        String dept  = sharedPreferences.getString("department",null);
//        department.setText(dept);
//        String year = sharedPreferences.getString("year",null);
//        membersince.setText(year);
//        Glide.with(this).load(profile_url).into(profile_image);
        return inflater.inflate(R.layout.activity_home_fragment, null);
    }



}
