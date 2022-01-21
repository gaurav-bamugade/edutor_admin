package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class newProductUploadActivity extends AppCompatActivity {
private String cate;
private Button btnChooseImg,btnUploadProduct;
private ImageView prod_img;

private TextInputEditText productName,productDescription,productPrice;
    private AutoCompleteTextView gender;
    private String currentuserid;
    private FirebaseAuth fireauth;
    private DatabaseReference firedb;
    private static final int GalleryPick=1;
    private StorageReference userprofileimageref;
    private ProgressDialog LoadingBar;
    private Uri imageuri;
    public RecyclerView cate_rc;
    private List<categoryModel> list;
    private categoryAdapter adap;
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference mr=dat.getReference();
    private Dialog loading,categoryDialog;
    private categoryAdapter  adapter;
    private CircleImageView addimg;
    private Button addbtn;
    private EditText categoryName;
    private Uri image;
    private Toolbar vr;
    private String downloadurl;
    private ImageView plusbtn,minusbtn;
    private TextView stockCtn;
private int totalquantity=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_upload);

        cate=getIntent().getStringExtra("cate");


        btnChooseImg=findViewById(R.id.btnchoose);
        btnUploadProduct=findViewById(R.id.uploadProduct);
        productName=findViewById(R.id.product_Name);
        productDescription=findViewById(R.id.product_description);
        productPrice=findViewById(R.id.product_price);
        plusbtn=findViewById(R.id.plus);
        minusbtn=findViewById(R.id.minus);
        stockCtn=findViewById(R.id.stockCt);
        prod_img=findViewById(R.id.prod_img);


        loading=new Dialog(this);
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loading.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loading.setCancelable(false);


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
                    Toast.makeText(newProductUploadActivity.this,"PLease select image", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(newProductUploadActivity.this,"something went wrong!!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(newProductUploadActivity.this,"something went wrong!!",Toast.LENGTH_SHORT).show();


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

            Toast.makeText(newProductUploadActivity.this,"Please Enter Required Credentials",Toast.LENGTH_SHORT).show();
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
            map.put("category",cate);


            FirebaseDatabase data=FirebaseDatabase.getInstance();
            data.getReference().child("products").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                        Toast.makeText(newProductUploadActivity.this,"Please Enter Required Credentials",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {

                        Toast.makeText(newProductUploadActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();

                    }
                    loading.dismiss();
                }
            });


        }
    }

}