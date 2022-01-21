package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.miniproj2admin.adapters.approvAdapter;
import com.example.miniproj2admin.models.approvModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ApprovalActivity extends AppCompatActivity {
    private RecyclerView approv_rc;
    List<approvModel> redeemmodel;
    approvAdapter redeemAda;
    private DatabaseReference usrref;
    private FirebaseAuth fireauth;
    private String currentuserid;
    private Toolbar vr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        approv_rc=findViewById(R.id.approv_rc);
        usrref=  FirebaseDatabase.getInstance().getReference();
        vr=findViewById(R.id.approv_bar);
        setSupportActionBar(vr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Approvals");
        redeemmodel=new ArrayList<>();
        LinearLayoutManager lm=new LinearLayoutManager(this);
        approv_rc.setLayoutManager(lm);
        redeemAda=new approvAdapter(redeemmodel,this);

        approv_rc.setAdapter(redeemAda);


             usrref.child("redeems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot prodsnapshot) {
                for(DataSnapshot ds:prodsnapshot.getChildren())
                {
                        String uid=ds.child("uid").getValue().toString();
                        String prodid=ds.child("prod_id").getValue().toString();;
                        String prodprice=ds.child("product_price").getValue().toString();
                        String prodName=ds.child("product_name").getValue().toString();
                        String prodimg=ds.child("product_image_url").getValue().toString();
                        String waitIcon=ds.child("iconimg").getValue().toString();
                        String approval=ds.child("approval").getValue().toString();
                        String pleaseWait=ds.child("pleaseWait").getValue().toString();
                        String redeemId=ds.child("redeem_id").getValue().toString();


                        usrref.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot usrsnapshot)
                            {
                                String usrname=usrsnapshot.child("username").getValue().toString();
                                String usrEmail=usrsnapshot.child("useremail").getValue().toString();

                                if(pleaseWait.equals("Congratulations!!!"))
                                {
                                    String appr="You have Approved!!!";
                                    redeemmodel.add(new approvModel(redeemId,usrEmail,usrname,uid,appr,prodid,prodimg,prodName,prodprice,pleaseWait,waitIcon));
                                    redeemAda.notifyDataSetChanged();
                                }
                                else
                                {
                                    String appr="Approval pending!!";
                                    redeemmodel.add(new approvModel(redeemId,usrEmail,usrname,uid,appr,prodid,prodimg,prodName,prodprice,pleaseWait,waitIcon));
                                    redeemAda.notifyDataSetChanged();
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error)
                            {

                            }
                        });


                    }
                }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i=new Intent(this,MainActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
}