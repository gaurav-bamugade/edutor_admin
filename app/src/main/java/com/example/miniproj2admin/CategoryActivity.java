package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.miniproj2admin.adapters.categoryAdapter;
import com.example.miniproj2admin.models.categoryModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryActivity extends AppCompatActivity {
    public RecyclerView cate_rc;
    public static List<categoryModel> list,searchmd;
    private categoryAdapter adap;

    private TextInputEditText categoryDesc,categoryName;
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference mr=dat.getReference();
    private Dialog loading,categoryDialog;
    private categoryAdapter  adapter;
    private CircleImageView addimg;
    private Button addbtn;
    private Uri image;
    private Toolbar vr;
    private String downloadurl;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        vr=findViewById(R.id.createCoursBar);
        setSupportActionBar(vr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Course");

        cate_rc=findViewById(R.id.cate_rc);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cate_rc.setLayoutManager(layoutManager);

        img=findViewById(R.id.add_category);

        loading=new Dialog(this);
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loading.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loading.setCancelable(false);


        setCategoryDialog();

        list=new ArrayList<>();
        adapter=new categoryAdapter(list,new categoryAdapter.deleteListener(){
             @Override
             public void onDelete(String key,int position) {
                 new AlertDialog.Builder(CategoryActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                         .setTitle("Delete Category")
                         .setMessage("Are you Sure , You want to delete this category")
                         .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 loading.show();
                                 mr.child("Categories").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull  Task<Void> task) {
                                         if(task.isSuccessful())
                                         {
                                                for(String setIds:list.get(position).getSet())
                                                {
                                                    mr.child("SETS").child(setIds).removeValue();
                                                }
                                             list.remove(position);
                                             adapter.notifyDataSetChanged();
                                             loading.dismiss();
                                           /*  mr.child("SETS").child(list.get(position).getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull  Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    list.remove(position);
                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(CategoryActivity.this,"Failed to delete", Toast.LENGTH_SHORT).show();

                                                                }
                                                                loading.dismiss();
                                                 }
                                             });*/

                                         }
                                         else
                                         {
                                             Toast.makeText(CategoryActivity.this,"Failed to delete", Toast.LENGTH_SHORT).show();

                                         }
                                         loading.dismiss();
                                     }
                                 });
                             }
                         })

                 .setNegativeButton("Cancel",null)
                 .setIcon(android.R.drawable.ic_dialog_alert)
                 .show();
             }
         });
        cate_rc.setAdapter(adapter);


        loading.show();
        mr.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
             for(DataSnapshot ds1:snapshot.getChildren())
             {
                 List<String> setss=new ArrayList<>();


                for(DataSnapshot ds:ds1.child("set").getChildren())
                 {
                     setss.add(ds.getKey());
                     String s=ds.child("set").getKey().toString();

                    // Toast.makeText(CategoryActivity.this,""+s,Toast.LENGTH_SHORT).show();
                 }
                 list.add(new categoryModel(ds1.child("url").getValue().toString(),ds1.child("name").getValue().toString(),
                             setss,ds1.getKey(),ds1.child("courseDesc").getValue().toString()));
             }
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading.dismiss();
                finish();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog.show();
            }
        });

    }


    private void setCategoryDialog()
    {
        categoryDialog =new Dialog(this);
        categoryDialog.setContentView(R.layout.add_category_dialog);
        categoryDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_box));
        categoryDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        categoryDialog.setCancelable(true);

        addimg=categoryDialog.findViewById(R.id.addImga);
        categoryName=categoryDialog.findViewById(R.id.categoryName);
        addbtn=categoryDialog.findViewById(R.id.add);
        categoryDesc=categoryDialog.findViewById(R.id.categoryDesc);

       addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i=new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,101);
            }
        });

       addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoryName.getText()==null || categoryName.getText().toString().isEmpty())
                {
                    categoryName.setError("Required");
                    return;
                }
                if(categoryDesc.getText()==null || categoryDesc.getText().toString().isEmpty())
                {
                    categoryDesc.setError("Required");
                    return;
                }
                for(categoryModel model:list)
                {
                   if( categoryName.getText().toString().equals(model.getName()))
                    {
                        categoryName.setError("category name already present");
                        return;
                    }
                }
                if(image==null)
                {
                    Toast.makeText(CategoryActivity.this,"PLease select image", Toast.LENGTH_SHORT).show();
                    return;
                }
                categoryDialog.dismiss();
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
            addimg.setImageURI(image);

        }

    }
    private void uploadData()
    {
        loading.show();
        StorageReference ref= FirebaseStorage.getInstance().getReference();
        StorageReference imgRef=ref.child("Categories").child(image.getLastPathSegment());

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
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(CategoryActivity.this,"something went wrong!!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CategoryActivity.this,"something went wrong!!",Toast.LENGTH_SHORT).show();
                    loading.dismiss();

                }
            }
        });

    }

    private void uploadCategory()
    {
        Map<String,Object> map =new HashMap<>();
        map.put("name",categoryName.getText().toString());
        map.put("set",0);
        map.put("url",downloadurl);
        map.put("courseDesc",categoryDesc.getText().toString());

        String id= UUID.randomUUID().toString();

        FirebaseDatabase data=FirebaseDatabase.getInstance();
        data.getReference().child("Categories").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    list.add(new categoryModel(downloadurl,categoryName.getText().toString(),new ArrayList<String>(),id,categoryDesc.getText().toString()));
                    adapter.notifyDataSetChanged();
                }
                else
                {

                }
                loading.dismiss();
            }
        });
    }
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.top_right_menu,menu);

        MenuItem item=menu.findItem(R.id.Seach);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                  /*filter(s);*/
                   img.setVisibility(View.GONE);
                }
                else
                {
                   /* list=new ArrayList<>();
                    adapter=new categoryAdapter(list,this);
                    cate_rc.setAdapter(adapter);*/
                    img.setVisibility(View.GONE);
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                }
                else
                {
                }
                return false;
            }
        });
        return true;
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
 /*  private void filter(String text)
    {
        for(productsModel ps:list)
        {
            if(ps.getProductName().equals(text))
            {
                searchModel.add(ps);
            }
        }
        proditemAda=new productItemAdapter(getContext(),searchModel);
        productsRecy.setAdapter(proditemAda);
        proditemAda.notifyDataSetChanged();
    }*/

  /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
     *//*   getMenuInflater().inflate(R.menu.set_menu,menu);
        return super.onCreateOptionsMenu(menu);*//*
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.set_menu,menu);
        MenuItem item=menu.findItem(R.id.addbtntop);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.addbtntop)
        {

           categoryDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }*/

/*public void viewUserList()
    {

        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("users");
        dbref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                usr_list.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    User usr=ds.getValue(User.class);
                    Log.d("postnew",ds.getValue().toString());
                    usr.setImage(ds.child("image").getValue().toString());
                    usr.setName(ds.child("name").getValue().toString());
                    usr.setStatus(ds.child("status").getValue().toString());
                    usr.setUid(ds.child("uid").getValue().toString());
                    usr_list.add(usr);
                }
                search_adap.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

   /* private void searchUsers(String query)
    {
        FirebaseUser us= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dvre=FirebaseDatabase.getInstance().getReference("users");
        dvre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                usr_list.clear();
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    User usr=ds.getValue(User.class);
                    if(!usr.getUid().equals(us.getUid()))
                    {
                        if(usr.getName().toLowerCase().contains(query.toLowerCase()))
                        {
                            Log.d("postnew",ds.getValue().toString());
                            usr.setImage(ds.child("image").getValue().toString());
                            usr.setName(ds.child("name").getValue().toString());
                            usr.setStatus(ds.child("status").getValue().toString());
                            usr.setUid(ds.child("uid").getValue().toString());
                            usr_list.add(usr);

                        }

                    }
                    search_adap=new SearchFriendAdapter(SearchFriendActivity.this,usr_list);
                    search_adap.notifyDataSetChanged();
                    search_friend_recycler.setAdapter(search_adap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       *//* FirebaseRecyclerOptions<User> fbopt=new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users"),User.class)
                .build();*//*
    }*/