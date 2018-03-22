package com.example.sahil.digitalclassroom.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Assignment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Sumir on 16-01-2018.
 */

public class AssignmentCreateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int PICKFILE_RESULT_CODE = 1;
    Uri filePath;
    String downloadUrl = null;
    String displayName = null; //of the file selected

    ImageButton dueDateButton;
    Button uploadButton;
    Button submitButton;
    Button cancelButton;

    TextView attachmentView;
    EditText nameView;
    EditText marksView;
    TextView dueDateView;
    Calendar calendar = Calendar.getInstance();

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10: if(grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED)
                configureButton();
                break;
            default: break;
        }
    }

    private void configureButton() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
            return;
        }
        else
        {
            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //call to open memory
                    openFolder();
                }
            });
        }
    }

    void openFolder(){
        final String[] ACCEPT_MIME_TYPES = {
                "application/pdf",
                "image/*",
                "application/msword"
        };
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES,ACCEPT_MIME_TYPES);
        startActivityForResult(Intent.createChooser(intent,"Select a file"),PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK) {
                    filePath = data.getData();
                    String strFileName = filePath.toString();
                    File myFile = new File(strFileName);
                    displayName = null;

                    if (strFileName.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = this.getContentResolver().query(filePath, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (strFileName.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    if(displayName != null) {
                        attachmentView.setText(displayName);
                        attachmentView.setTextColor(Color.BLACK);
                    }

                }
                break;
        }
    }

    private void uploadAssignment(final String Name, final double Marks) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("files/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AssignmentCreateActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            downloadUrl = taskSnapshot.getMetadata().getDownloadUrl().toString();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference("/assignments");

                            DatabaseReference newAssignmentRef = ref.push();
                            String assignmentId = newAssignmentRef.getKey();

                            long dueDate = calendar.getTimeInMillis();
                            long uploadedAt = System.currentTimeMillis();

                            Assignment assignment = new Assignment(assignmentId, Name, downloadUrl,dueDate, uploadedAt, Marks, displayName);
                            newAssignmentRef.setValue(assignment);

                            startActivity(new Intent(AssignmentCreateActivity.this,AssignmentActivity.class));
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AssignmentCreateActivity.this, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_create);

        attachmentView = (TextView) findViewById(R.id.assignmentCreateAttachmentFiles);
        dueDateView = (TextView) findViewById(R.id.assignmentDueDateText);
        calendar.setTime(new Date());
        dueDateView.setText(DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime()));

        dueDateButton = (ImageButton) findViewById(R.id.assignmentDueDateButton);
        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadButton = (Button) findViewById(R.id.assignmentCreateAttachment);
        configureButton();

        cancelButton = (Button) findViewById(R.id.assignmentCreateCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AssignmentCreateActivity.this, "Cancelled",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(AssignmentCreateActivity.this,AssignmentActivity.class));
                finish();
            }
        });

        submitButton = (Button) findViewById(R.id.assignmentCreateSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View focusView = null;
                boolean cancel = false;

                nameView = (EditText) findViewById(R.id.assignmentCreateName);
                marksView = (EditText) findViewById(R.id.assignmentCreateMarks);

                if(displayName == null){
                    attachmentView.setText(R.string.no_attached_file);
                    attachmentView.setTextColor(Color.RED);
                    focusView = attachmentView;
                    cancel = true;
                }

                if(dueDateView.getText() == "")
                {
                    dueDateView.setText(R.string.old_date_set);
                    dueDateView.setTextColor(Color.RED);
                    focusView = dueDateView;
                    cancel = true;
                }

                String Name = nameView.getText().toString();
                // Check if the user entered name of the assignment.
                if (TextUtils.isEmpty(Name)) {
                    nameView.setError(getString(R.string.error_field_required));
                    focusView = nameView;
                    cancel = true;
                }


                String marksString = marksView.getText().toString();
                double Marks = 0.0;
                // Check if marks of assignment are entered or not.
                if (TextUtils.isEmpty(marksString)) {
                    Marks = 0.0;
                }
                else {
                    Marks = (double) Double.parseDouble(marksString);
                }


                if (cancel) {
                    // There was an error; don't attempt sending and focus the first form field with an error.
                    focusView.requestFocus();

                } else {
                    uploadAssignment(Name,Marks);
                }

            }
        });
    }

    public static boolean isBehindCurrentDate(Calendar calendar){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        if(calendar.getTimeInMillis() < now.getTimeInMillis())

        if(now.get(Calendar.YEAR) > calendar.get(Calendar.YEAR))
            return true;
        else if(now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && now.get(Calendar.MONTH) > calendar.get(Calendar.MONTH))
            return true;
        else if(now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) > calendar.get(Calendar.DAY_OF_MONTH))
            return true;

        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        if(isBehindCurrentDate(calendar)) {
            dueDateView.setText("");
            dueDateView.setText(R.string.old_date_set);
            dueDateView.setTextColor(Color.RED);
        }
        else{
            String date = DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime());
            dueDateView.setText(date);
            dueDateView.setTextColor(Color.BLACK);
        }

    }

    //Fragment of date picker for picking date
    public static class DatePickerFragment extends DialogFragment {
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

}
