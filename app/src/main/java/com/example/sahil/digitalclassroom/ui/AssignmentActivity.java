package com.example.sahil.digitalclassroom.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.adapter.AssignmentListAdapter;
import com.example.sahil.digitalclassroom.adapter.TakeAttendanceAdapter;
import com.example.sahil.digitalclassroom.model.Assignment;
import com.example.sahil.digitalclassroom.model.AssignmentSubmit;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by Sumir on 16-01-2018.
 */

public class AssignmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssignmentListAdapter mAdapter;
    private ArrayList<Assignment> AssignmentList;
    Uri filePath = null;
    String displayName = null;
    public static Integer whichWasClicked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        AssignmentList = new ArrayList();
        getDataIntoAssignmentList();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_assignment);

        mAdapter = new AssignmentListAdapter(AssignmentList,AssignmentActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar_assign);
//        setSupportActionBar(toolbar);
        //if teacher
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.floatingActionButtonAssignment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create new assignment activity
                startActivity(new Intent(AssignmentActivity.this,AssignmentCreateActivity.class));
            }
        });
//        else hide this button

    }

    public void getDataIntoAssignmentList(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("assignments");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Assignment assignment = dataSnapshot.getValue(Assignment.class);
                progressDialog.dismiss();
                if(assignment != null)
                {
                    AssignmentList.add(assignment);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case 1:
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure you want to upload " + displayName);

                        builder.setNeutralButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AssignmentActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                uploadSubmission();
                            }
                        });
                        builder.show();
                    }

                }
                break;
        }
    }
    private void uploadSubmission() {

        //Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

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
                            Toast.makeText(AssignmentActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            String downloadUrl = taskSnapshot.getMetadata().getDownloadUrl().toString();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference("/assignmentSubmissions");

                            DatabaseReference newAssignmentRef = ref.push();
                            String _id = newAssignmentRef.getKey();
                            String assign_id = AssignmentList.get(whichWasClicked).getAssign_id();
                            long submittedAt = System.currentTimeMillis();
                            String assignName = AssignmentList.get(whichWasClicked).getName();
                            AssignmentSubmit assignmentSubmit = new AssignmentSubmit(_id, assign_id,"asasd", assignName, assign_id+"_asasd", downloadUrl, submittedAt, 0.0,false);
                            newAssignmentRef.setValue(assignmentSubmit);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AssignmentActivity.this, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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


}
