package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.miniproj2admin.adapters.gridAdapter;
import com.example.miniproj2admin.models.questionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class setActivity extends AppCompatActivity {

    private GridView gridView;
    private int position;
    private Dialog loading;
    gridAdapter adapter;
    private String categoryname;
    private DatabaseReference myref;
    private Toolbar bar;
    private Button addvideos;
    private List<String> sets;
    private String gettingkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

       bar=findViewById(R.id.toolbar3);
       setSupportActionBar(bar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        myref=FirebaseDatabase.getInstance().getReference();

        loading=new Dialog(this);
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loading.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loading.setCancelable(false);
        categoryname= getIntent().getStringExtra("title");
        gridView=findViewById(R.id.gridView1);
        position=getIntent().getIntExtra("position",0);
        addvideos=findViewById(R.id.addVideos);

        gettingkey=getIntent().getStringExtra("key");
        String id= UUID.randomUUID().toString();

        sets=CategoryActivity.list.get(getIntent().getIntExtra("position",0)).getSet();
        addvideos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(setActivity.this,videosAddingActivity.class);
                i.putExtra("key",gettingkey);
                startActivity(i);
            }
        });



        Log.d("setrange",String.valueOf(position));
        adapter=new gridAdapter(sets,categoryname, new gridAdapter.GridListener() {
            @Override
            public void addSet() {
                loading.show();
                FirebaseDatabase firebd=FirebaseDatabase.getInstance();
                firebd.getReference().child("Categories").child(getIntent().getStringExtra("key")).child("set").child(id).setValue("SET ID").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            sets.add(id);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(setActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        loading.dismiss();
                    }
                });
            }

            @Override
            public void onLongClick(String setId,int position)
            {
                new AlertDialog.Builder(setActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                        .setTitle("Delete Set "+ position)
                        .setMessage("Are you Sure , You want to delete this quesiton?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loading.show();
                                loading.show();
                                myref.child("SETS").child(setId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            myref.child("Categories").child(CategoryActivity.list.get(getIntent().getIntExtra("position",0)).getKey())
                                                    .child("set").child(setId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        adapter.sets.remove(setId);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    else
                                                    {
                                                         Toast.makeText(setActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                    }
                                                    loading.dismiss();
                                                }
                                            });

                                        }
                                        else
                                        {
                                            Toast.makeText(setActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
                                            loading.dismiss();
                                        }

                                    }
                                });

                                 /*   @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });*/
                            }
                        })

                        .setNegativeButton("Cancel",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        gridView.setAdapter(adapter);
    }
}