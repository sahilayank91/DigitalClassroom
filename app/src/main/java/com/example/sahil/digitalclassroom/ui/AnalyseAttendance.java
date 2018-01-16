package com.example.sahil.digitalclassroom.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.sahil.digitalclassroom.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AnalyseAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private Button fromDate,toDate;
    public Date fromDateData,toDateData;
    public int flag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_attendance);
        fromDate = (Button) findViewById(R.id.button_from_date);
        toDate = (Button) findViewById(R.id.button_to_date);
        //Below functions to pick the date accordingly
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                DialogFragment datePicker = new DatePickerFragmentFrom();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                DialogFragment datePicker = new DatePickerFragmentTo();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

    }
    
    //To set the date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        if (flag==1) {
            String From_button_text = "From : " + DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime());
            fromDate.setText(From_button_text);
            fromDateData = calendar.getTime();//Get the date time int the desired format
        }
        else {
            String From_button_text = "To : " + DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime());
            toDate.setText(From_button_text);
            toDateData = calendar.getTime();//Get the date time int the desired format
        }
    }
    
    //Two fragment of date picker for picking date 
    public static class DatePickerFragmentTo extends DialogFragment {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
        }
    }

    public static class DatePickerFragmentFrom extends DialogFragment {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,1,1);
        }
    }
}
