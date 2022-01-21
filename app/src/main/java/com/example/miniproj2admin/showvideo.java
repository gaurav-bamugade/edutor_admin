package com.example.miniproj2admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*import com.example.miniproj2admin.models.memberModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;*/
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class showvideo extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showvideo);


        recyclerView= findViewById(R.id.recycler_exo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("videos");

    }

    @Override
    protected void onStart() {
        super.onStart();

       /* FirebaseRecyclerOptions<memberModel> options=
                new FirebaseRecyclerOptions.Builder<memberModel>()
                .setQuery(databaseReference,memberModel.class)
                .build();

        FirebaseRecyclerAdapter<memberModel,exoViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<memberModel, exoViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull exoViewHolder exo_viewHolder, int i, @NonNull memberModel member) {
                        exo_viewHolder.setExoplayer(getApplication(),member.getName(),member.getVideourl());
                    }

                    @NonNull
                    @Override
                    public exoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view= LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.exo_item_layout,parent,false);

                        return new exoViewHolder(view);
                    }
                };
                
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);*/

    }
}