package com.example.sahil.digitalclassroom.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.UUID;

/**
 * Created by Sumir on 16-01-2018.
 */

public class AssignmentCreateActivity extends AppCompatActivity {

    private static final int PICKFILE_RESULT_CODE = 1;
    Uri filePath;
    Uri downloadUrl;

    Button dueDateButton;
    Button uploadButton;
    Button submitButton;
    TextView attachment;
    TextView name;
    TextView marks;

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
                if(resultCode==RESULT_OK){
                    filePath = data.getData();
//                    attachment.append(FilePath);
                    Log.e("data",data.toString());
                }
                break;
        }
    }

    private void uploadAttachments() {

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

                            downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AssignmentCreateActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        attachment = (TextView) findViewById(R.id.assignmentCreateAttachmentFiles);
        dueDateButton = (Button) findViewById(R.id.assignmentDueDate);
        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo open data picker
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadButton = (Button) findViewById(R.id.assignmentCreateAttachment);
        configureButton();

        submitButton = (Button) findViewById(R.id.assignmentCreateSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadAttachments();
                name = (TextView) findViewById(R.id.assignmentCreateName);
                marks = (TextView) findViewById(R.id.assignmentCreateMarks);

                String Name = name.getText().toString();
                double Marks = (double) Double.parseDouble(marks.getText().toString());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("/assignments");

                DatabaseReference newAssignmentRef = ref.push();

                String assignmentId = newAssignmentRef.getKey();
                Assignment assignment = new Assignment(assignmentId,Name,downloadUrl,Marks);

                newAssignmentRef.setValue(assignment);

            }
        });
    }
}
