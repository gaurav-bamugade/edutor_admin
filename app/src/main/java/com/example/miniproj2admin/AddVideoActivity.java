package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.miniproj2admin.models.memberModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddVideoActivity extends AppCompatActivity {
    int PICK_VIDEO=1;
    VideoView videoView;
    Button btn;
    ProgressBar progressBar;
    EditText editText;
    Uri videUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    memberModel m;
    UploadTask uploadTask;
    String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);


        m =new memberModel();
        storageReference= FirebaseStorage.getInstance().getReference().child("Videos");
        databaseReference= FirebaseDatabase.getInstance().getReference();

        videoView=findViewById(R.id.stream_vid);
        btn=findViewById(R.id.upload_btn);
        progressBar=findViewById(R.id.progress_amin);
        editText=findViewById(R.id.et_video_name);
        mediaController=new MediaController(this);
        courseId=getIntent().getStringExtra("key");
        videoView.setMediaController(mediaController);
        videoView.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText()==null || editText.getText().toString().isEmpty())
                {
                    editText.setError("Required");
                    return;
                }
                if(videUri==null)
                {
                    Toast.makeText(AddVideoActivity.this,"PLease select Video", Toast.LENGTH_SHORT).show();
                    return;
                }
                UploadVideo();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_VIDEO|| resultCode==RESULT_OK||data!=null ||data.getData() !=null)
        {
            videUri=data.getData();
            videoView.setVideoURI(videUri);
        }

    }
    public void chooseVideo(View view) {
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO);

    }

    private String getExt(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }



    private void UploadVideo()
    {
        String videoname=editText.getText().toString();
        String search=editText.getText().toString().toLowerCase();

        if(!TextUtils.isEmpty(videoname))
        {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference ref=storageReference.child(System.currentTimeMillis()+"."+getExt(videUri));
            uploadTask=ref.putFile(videUri);

            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful())
                    {
                        String id= UUID.randomUUID().toString();
                        Uri downloadurl=task.getResult();
                        progressBar.setVisibility(View.INVISIBLE);
                        m.setTopicName(videoname);
                        m.setVideourl(downloadurl.toString());
                        m.setSearch(search);
                        databaseReference.child("Categories").child(courseId).child("videos").child(id).setValue(m);
                        Toast.makeText(AddVideoActivity.this,"You Video has been uploaded",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(AddVideoActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(AddVideoActivity.this,"Failed twice",Toast.LENGTH_SHORT).show();
        }

    }


}