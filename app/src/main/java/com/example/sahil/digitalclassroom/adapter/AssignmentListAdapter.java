package com.example.sahil.digitalclassroom.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Assignment;
import com.example.sahil.digitalclassroom.ui.AssignmentActivity;
import com.example.sahil.digitalclassroom.ui.AssignmentCreateActivity;
import com.example.sahil.digitalclassroom.ui.AssignmentSubmittedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.sahil.digitalclassroom.ui.AssignmentCreateActivity.isBehindCurrentDate;

/**
 * Created by Sumir on 19-01-2018.
 */

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.MyViewHolder>{
    private static final int PICKFILE_RESULT_CODE = 1;
    private Uri filePath = null;
    private String displayName = null;
    boolean alreadySent = false;

    //make list of user with percentage
    private ArrayList<Assignment> AssignmentList;
    private Context context;
    public AssignmentListAdapter(ArrayList<Assignment> AssignmentList, Context context){
        this.AssignmentList = AssignmentList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView assignmentName;
        TextView assignmentDueDateText;
        TextView assignmentDueDate;
        TextView assignmentMarks;
        Button assignmentDownload;
        Button assignmentUpload;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.list_assignment);
            assignmentName = (TextView) view.findViewById(R.id.assignmentListName);
            assignmentDueDateText = (TextView) view.findViewById(R.id.assignmentListDueDateText);
            assignmentDueDate = (TextView) view.findViewById(R.id.assignmentListDueDate);
            assignmentMarks = (TextView) view.findViewById(R.id.assignmentListMarks);
            assignmentDownload = (Button) view.findViewById(R.id.assignmentDownload);
            assignmentUpload = (Button) view.findViewById(R.id.assignmentUpload);
        }
    }

    @Override
    public AssignmentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_assignment, parent, false);

        return new AssignmentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AssignmentListAdapter.MyViewHolder holder, final int position) {
        final Assignment assignment = AssignmentList.get(position);

        final int role = 2; // 1 is for teacher
        if(role == 1) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context,AssignmentSubmittedActivity.class).putExtra("AssignmentId",assignment.getAssign_id()));
                }
            });
        }

        holder.assignmentName.setText(assignment.getName());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(assignment.getDueDate()));
        holder.assignmentDueDate.setText(DateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(calendar.getTime()));

        if(isBehindCurrentDate(calendar)) {
            holder.assignmentDueDate.setTextColor(Color.parseColor("#C62828"));
            holder.assignmentDueDateText.setText("");
            holder.assignmentDueDate.setText("Due Date Over");
            holder.assignmentUpload.setVisibility(View.GONE);
        }
        else{
            holder.assignmentDueDate.setTextColor(Color.parseColor("#2E7D32"));
        }

        double Marks = assignment.getMarks();
        if(Marks - (int)Marks != 0.0)
            holder.assignmentMarks.setText(Marks + " Marks");
        else
            holder.assignmentMarks.setText((int)Marks + " Marks");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("assignmentSubmissions").orderByChild("id_studentId").equalTo(assignment.getAssign_id() + "_" + "asasd");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0){
                    alreadySent = true;
                    if(role != 1) {
                        holder.assignmentUpload.setVisibility(View.GONE);
                    }
                }
                else{
                    alreadySent = false;

                    holder.assignmentDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //download respective assignment;
                        }
                    });

                    holder.assignmentUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //upload an assignment to assignment submit
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Upload File");

                            final TextView textView = new TextView(context);

                            builder.setNeutralButton("Upload",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AssignmentActivity.whichWasClicked = position;
                                    //get file name to upload
                                    configureButton(context);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(role == 1) {
            holder.assignmentUpload.setVisibility(View.GONE);
            holder.assignmentDownload.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return AssignmentList.size();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10: if(grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED)
                configureButton(context);
                break;
            default: break;
        }
    }

    private void configureButton(Context context) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Give Permissions");
            builder.setMessage("Please give permissions to this app to upload file from storage.\nGo to Settings>Apps>Permissions");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
            return;
        }
        else
        {
            //call to open memory
            openFolder();
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
        ((AssignmentActivity)context).startActivityForResult(Intent.createChooser(intent,"Select a file"),PICKFILE_RESULT_CODE);
    }
}
