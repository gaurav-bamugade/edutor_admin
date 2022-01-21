package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproj2admin.adapters.categoryAdapter;
import com.example.miniproj2admin.models.categoryModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProdDetailsActivity extends AppCompatActivity {

    private ImageView prodImg;
    private TextView prodName,prodprice,prodcategory,proddesc;
    private DatabaseReference myref;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prod);

        prodImg=findViewById(R.id.product_detail_image);
        prodName=findViewById(R.id.details_prod_name);
        prodprice=findViewById(R.id.details_prod_price);
        prodcategory=findViewById(R.id.details_prod_category);
        proddesc=findViewById(R.id.details_prod_desc);
        myref=  FirebaseDatabase.getInstance().getReference();
        pid=getIntent().getStringExtra("prodid");



        myref.child("products").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot ds) {

                //String prodid=ds.child("prod_id").getValue().toString();;
                String prodDesc=ds.child("product_description").getValue().toString();
                String prodNames=ds.child("product_name").getValue().toString();
                String prodPrice=ds.child("product_price").getValue().toString();
                //String stockCount=ds.child("stock_count").getValue().toString();
                String prodCate=ds.child("category").getValue().toString();
                String prodimg=ds.child("product_image_url").getValue().toString();
                Picasso.get().load(prodimg).placeholder(R.drawable.theme2).into(prodImg);
                prodName.setText(prodNames);
                prodprice.setText(prodPrice);
                prodcategory.setText(prodCate);
                proddesc.setText(prodDesc);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}