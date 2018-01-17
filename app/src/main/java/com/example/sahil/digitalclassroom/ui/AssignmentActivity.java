package com.example.sahil.digitalclassroom.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.sahil.digitalclassroom.R;

/**
 * Created by Sumir on 16-01-2018.
 */

public class AssignmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
//        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar_assign);
//        setSupportActionBar(toolbar);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.floatingActionButtonAssignment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show create new assignment activity
                startActivity(new Intent(AssignmentActivity.this,AssignmentCreateActivity.class));
            }
        });
    }

}
