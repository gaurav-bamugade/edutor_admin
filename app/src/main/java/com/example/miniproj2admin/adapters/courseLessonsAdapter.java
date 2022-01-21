package com.example.miniproj2admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.miniproj2admin.R;
import com.example.miniproj2admin.ViewVideoActivity;
import com.example.miniproj2admin.models.courseLessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class courseLessonsAdapter extends RecyclerView.Adapter<courseLessonsAdapter.ViewHolder> {
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference mr=dat.getReference();
    Context context;
    List<courseLessonsModel> ls;
    public courseLessonsAdapter(Context context, List<courseLessonsModel> ls) {
        this.context = context;
        this.ls = ls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.drv_item_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        courseLessonsModel l=ls.get(position);
        holder.name_of_the_video.setText(l.getTopicName());
        holder.setData(ls.get(position).getKey(),ls.get(position).getVideourl());


    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name_of_the_video;
        ImageView deletevideo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name_of_the_video=itemView.findViewById(R.id.name_of_the_video);
            deletevideo=itemView.findViewById(R.id.deletevideo);


        }

        private void setData(String key,String Videourl)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ints=new Intent(context, ViewVideoActivity.class);
                    ints.putExtra("key",key);
                    ints.putExtra("videourl",Videourl);
                    context.startActivity(ints);
                }
            });
            deletevideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                mr.child("Categories").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            ds.getKey();
                            mr.child("Categories").child(ds.getKey()).child("videos").child(key).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                }
            });
        }
    }
}
