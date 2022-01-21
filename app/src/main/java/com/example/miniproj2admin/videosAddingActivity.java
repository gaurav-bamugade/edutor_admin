package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.miniproj2admin.adapters.courseLessonsAdapter;
import com.example.miniproj2admin.models.courseLessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class videosAddingActivity extends AppCompatActivity {
    private RecyclerView rc;
    private courseLessonsAdapter lessonAdapter;
    private List<courseLessonsModel> lm;
    private DatabaseReference ref;
    private Button addvids;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_adding);

        addvids=findViewById(R.id.addvids);
        rc=findViewById(R.id.lesson_rc);
        rc.setHasFixedSize(true);
        LinearLayoutManager ln=new LinearLayoutManager(this);
        key=getIntent().getStringExtra("key");
        rc.setLayoutManager(ln);
        lm=new ArrayList<>();
        lessonAdapter=new courseLessonsAdapter(videosAddingActivity.this,lm);
        rc.setAdapter(lessonAdapter);
        ref= FirebaseDatabase.getInstance().getReference();
        addvids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(videosAddingActivity.this,AddVideoActivity.class);
                i.putExtra("key",key);
                startActivity(i);
            }
        });
        Log.d("keytestivid",key);

        ref.child("Categories").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                lm.clear();

                    for(DataSnapshot ds:snapshot.child("videos").getChildren())
                    {
                        String vidname = ds.child("search").getValue().toString();
                        String search = ds.child("topicName").getValue().toString();
                        String url = ds.child("videourl").getValue().toString();
                        for(DataSnapshot ds2:ds.getChildren())
                        {

                        }
                        lm.add(new courseLessonsModel(vidname,url,search,ds.getKey()));
                    }


                lessonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}