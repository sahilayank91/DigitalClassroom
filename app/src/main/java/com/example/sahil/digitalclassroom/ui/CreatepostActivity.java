package com.example.sahil.digitalclassroom.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahil.digitalclassroom.R;
import com.example.sahil.digitalclassroom.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CreatepostActivity extends AppCompatActivity {

    private Uri filePath;
    private ImageView imageView;
    private Button submit;
    private EditText text;
    private Context context;
    private Uri downloadUrl;
    private String image_url;
    private String file_url;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_FILE_REQUEST = 51;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    private CardView addFile;
    private TextView fileList ;
    private int flag = 0;
    private String group_id;
    private String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        group_id = getIntent().getStringExtra("group_id");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        setContentView(R.layout.activity_createpost);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.image);
        addFile = (CardView)findViewById(R.id.addFile);
        fileList = (TextView)findViewById(R.id.fileList);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        submit = (Button)findViewById(R.id.submit_milestone_button);
        text = (EditText) findViewById(R.id.text);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void chooseFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*" +
                "*/*");
        startActivityForResult(Intent.createChooser(intent,"Select File"),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            flag = 0;
            uploadFile();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if(requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            flag = 1;
            filePath = data.getData();
            String path = filePath.toString();
            File file = new File(filePath.toString());

            if(path.startsWith("content://")){
                Cursor cursor = null;
                try{
                    cursor = context.getContentResolver().query(filePath,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                       fileName= cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }finally {
                    cursor.close();
                }
            }else if(fileName.startsWith("file://")){
                fileName = file.getName();
            }

            fileList.setText(fileName);
            uploadFile();

        }
    }

    private void uploadPost(){
        String userText = text.getText().toString();

        String userId = sharedPreferences.getString("userId","");
        String username = sharedPreferences.getString("username","");
        String dept  = sharedPreferences.getString("department","");
        long time= System.currentTimeMillis();

                                /*Getting firebase Database configuration*/
        FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    /*User Reference*/
        DatabaseReference ref = database.getReference("/posts");

        DatabaseReference newPostRef = ref.push();

        String post_id = newPostRef.getKey();
        /*Uploading the Post Data to the Database*/
        Post post = new Post(post_id,userId,time,userText,image_url,file_url,username,dept,group_id);
        newPostRef.setValue(post);


        /*Coming back to Dashboard*/
        Intent intent= new Intent(CreatepostActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
    private void uploadFile() {

        if(filePath != null) {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String path = "";

            /*Defining paths for images and other files*/
            if (flag == 0) {
                path = "images/" + UUID.randomUUID().toString();
            }else{
                path = "files/" +  UUID.randomUUID().toString();
            }

                StorageReference ref = storageReference.child(path);
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();

                                if(flag==0){
                                    image_url = downloadUrl.toString();
                                }else{
                                    file_url = downloadUrl.toString();
                                }
                                Toast.makeText(CreatepostActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(CreatepostActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

        }
    }

}
