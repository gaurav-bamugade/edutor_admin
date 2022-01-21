package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.apache.xmlbeans.impl.piccolo.xml.Piccolo;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class editProdDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String proid;
    private Button btnChooseImg,btnUploadProduct;
    private ImageView prod_img;
    private Dialog loading;
    private ImageView plusbtn,minusbtn;
    private Uri image;
    private TextInputEditText productName,productDescription,productPrice;
    private String downloadurl;
    String catetext;
    private Spinner cateSpinner;
    private DatabaseReference firedb;
    private TextView stockCtn;
    private int totalquantity=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prod_details);

        productName=findViewById(R.id.edit_product_Name);
        productDescription =findViewById(R.id.edit_product_description);
        productPrice=findViewById(R.id.edit_product_price);
        prod_img=findViewById(R.id.edit_prod_img);
        btnChooseImg=findViewById(R.id.edit_btnchoose);
        btnUploadProduct=findViewById(R.id.edit_uploadProduct);
        plusbtn=findViewById(R.id.edit_plus);
        minusbtn=findViewById(R.id.edit_minus);
        cateSpinner=findViewById(R.id.prod_cat_spinner);
        firedb= FirebaseDatabase.getInstance().getReference();
        proid=getIntent().getStringExtra("prodid");

        ArrayAdapter<CharSequence> adt=ArrayAdapter.createFromResource(this,R.array.category, android.R.layout.simple_spinner_item);
        adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cateSpinner.setAdapter(adt);
        cateSpinner.setOnItemSelectedListener(this);


        loading=new Dialog(this);
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loading.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loading.setCancelable(false);

        firedb.child("products").child(proid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {

                String  category=Snapshot.child("category").getValue().toString();

                String  product_description=Snapshot.child("product_description").getValue().toString();

                String  product_image_url=Snapshot.child("product_image_url").getValue().toString();

                String  product_name=Snapshot.child("product_name").getValue().toString();

                String  product_price=Snapshot.child("product_price").getValue().toString();
                int currentstocks=Integer.parseInt(Snapshot.child("stock_count").getValue().toString());
                totalquantity = currentstocks;

                productName.setText(product_name);
                productDescription .setText(product_description);
                Picasso.get().load( product_image_url).into(prod_img);
                productPrice.setText(product_price);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalquantity<10)
                {
                    totalquantity++;
                    stockCtn.setText(String.valueOf(totalquantity));
                }
            }
        });

        minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalquantity>0)
                {
                    totalquantity--;
                    stockCtn.setText(String.valueOf(totalquantity));
                }
            }
        });

        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent i=new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,101);
            }
        });


        btnUploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productName.getText().toString().isEmpty() && productDescription.getText().toString().isEmpty() && productPrice.getText().toString().isEmpty())
                {
                    productName.setError("Required");
                    productDescription.setError("Required");
                    productPrice.setError("Required");


                    return;
                }
                if(image==null)
                {
                    Toast.makeText(editProdDetailsActivity.this,"PLease select image", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadData();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            image=data.getData();
            prod_img.setImageURI(image);

        }
    }
    private void uploadData()
    {
        loading.show();
        StorageReference ref= FirebaseStorage.getInstance().getReference();
        StorageReference imgRef=ref.child("prodimg").child(image.getLastPathSegment());

        UploadTask uploadTask = imgRef.putFile(image);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull  Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadurl=task.getResult().toString();
                            uploadCategory();
                            finish();
                        }
                        else
                        {

                            Toast.makeText(editProdDetailsActivity.this,"something went wrong!!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    Toast.makeText(editProdDetailsActivity.this,"something went wrong!!",Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    private void uploadCategory()
    {
        String prodName=productName.getText().toString();
        String prodDesc=productDescription.getText().toString();
        String prodPrice=productPrice.getText().toString();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());

        if(TextUtils.isEmpty(prodName) && TextUtils.isEmpty(prodDesc) &&TextUtils.isEmpty(prodPrice) &&TextUtils.isEmpty(prodDesc) ){

            Toast.makeText(editProdDetailsActivity.this,"Please Enter Required Credentials",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            String id= UUID.randomUUID().toString();
            HashMap<String,Object> map =new HashMap<>();
            map.put("prod_id",id);
            map.put("product_name",prodName);
            map.put("product_description",prodDesc);
            map.put("product_price",Integer.parseInt(prodPrice));
            map.put("stock_count",totalquantity);
            map.put("time",currentDateandTime);
            map.put("product_image_url",downloadurl);
            map.put("category",catetext);


            FirebaseDatabase data=FirebaseDatabase.getInstance();
            data.getReference().child("products").child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                        Toast.makeText(editProdDetailsActivity.this,"Please Enter Required Credentials",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {

                        Toast.makeText(editProdDetailsActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();

                    }
                    loading.dismiss();
                }
            });


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        catetext=parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}