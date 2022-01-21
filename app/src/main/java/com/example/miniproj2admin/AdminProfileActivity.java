package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfileActivity extends AppCompatActivity {
    private CircleImageView circlimage;
    private ImageView editPic;
    private TextInputEditText prof_name,prof_num,dob,useremail;

    private AutoCompleteTextView gender;
    private String currentuserid;
    private FirebaseAuth fireauth;
    private DatabaseReference firedb;
    private static final int GalleryPick=1;
    private StorageReference userprofileimageref;
    private ProgressDialog LoadingBar;
    private Uri imageuri;
    private Toolbar toolbar;

    private DatePickerDialog datePickerDialog;

    private MaterialButton update_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        fireauth= FirebaseAuth.getInstance();
       // currentuserid=fireauth.getCurrentUser().getUid();
        firedb= FirebaseDatabase.getInstance().getReference();



        userprofileimageref= FirebaseStorage.getInstance().getReference().child("Profile Images");

        initialize();
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });
        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery= new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GalleryPick);
            }
        });
        firedb.child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()&& (dataSnapshot.hasChild("Email"))))
                {
                    String dbcurrentuseremail= dataSnapshot.child("Email").getValue().toString();
                    useremail.setText(dbcurrentuseremail);
                }
                if((dataSnapshot.exists()&&(dataSnapshot.hasChild("username")) && (dataSnapshot.hasChild("Email")) )){
                    String dbcurrentusername= dataSnapshot.child("username").getValue().toString();
                  //  String retriveprofimg= dataSnapshot.child("image").getValue().toString();
                    prof_name.setText(dbcurrentusername);
                   // Picasso.get().load(retriveprofimg).into(circlimage);
                }
                if((dataSnapshot.exists()&&(dataSnapshot.hasChild("dob"))))
                {
                    String dbcurrentuserdob= dataSnapshot.child("dob").getValue().toString();
                    dob.setText(dbcurrentuserdob);
                }
                if((dataSnapshot.exists()&&(dataSnapshot.hasChild("userphonno"))))
                {
                    String dbcurrentuserPhone= dataSnapshot.child("userphonno").getValue().toString();
                    prof_num.setText(dbcurrentuserPhone);
                }

                if((dataSnapshot.exists()&&(dataSnapshot.hasChild("username")))){
                    String dbcurrentusername= dataSnapshot.child("username").getValue().toString();
                    prof_name.setText(dbcurrentusername);

                }
                else {
                    Toast.makeText(AdminProfileActivity.this,"Please update your profile..",Toast.LENGTH_SHORT).show();
                }
                if((dataSnapshot.exists()&&(dataSnapshot.hasChild("image"))  )){

                    String retriveprofimg= dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(retriveprofimg).into(circlimage);
                }
                else
                {
                    Picasso.get().load(R.drawable.adminicon).into(circlimage);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void initialize() {

        circlimage = findViewById(R.id.custom_profile_image);
        editPic=findViewById(R.id.edit_pic);
        dob=findViewById(R.id.et_enter_birthday_of_user);
        useremail=findViewById(R.id.et_enter_email_id_of_user);


   /*     gender=findViewById(R.id.gender);
        String genderstr[]=getResources().getStringArray(R.array.gender);
        ArrayAdapter arr=new ArrayAdapter(EditProfileActivity.this,R.layout.dropdown_items,genderstr);
        gender.setAdapter(arr);
*/


        MaterialDatePicker.Builder build=MaterialDatePicker.Builder.datePicker();
        build.setTitleText("Select A Date");
        final MaterialDatePicker materialDatePicker=build.build();
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection)
                    {
                        dob.setText(materialDatePicker.getHeaderText());
                    }
                });


            }
        });



        prof_name= findViewById(R.id.et_enter_name_of_user);
        prof_num= findViewById(R.id.et_enter_mobile_number_of_user);
        update_btn= findViewById(R.id.save_btn);
        LoadingBar=new ProgressDialog(this);




     /*   setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Account Settings");*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            imageuri=data.getData();

        }
        if(requestCode== GalleryPick && resultCode== RESULT_OK && data!=null){
            imageuri= data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(AdminProfileActivity.this);
            //circlimage.setImageURI(imageuri);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                LoadingBar.setTitle("Set Profile Image");
                LoadingBar.setMessage("Please wait, while your profile image is updating...");
                LoadingBar.setCanceledOnTouchOutside(false);
                LoadingBar.show();
                Uri resultUri = result.getUri();

                StorageReference filepath= userprofileimageref.child(currentuserid+ ".jpg");

                final UploadTask uploadTask = filepath.putFile(resultUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                String downloadUrl = uri.toString();
                                HashMap<String,Object> profileuri = new HashMap<>();
                                profileuri.put("image" , downloadUrl);
                                firedb.child("Admin").updateChildren(profileuri)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                LoadingBar.dismiss();
                                                Toast.makeText(AdminProfileActivity.this, "Profile Image Updated Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        }
    }

    private void UpdateSettings(){
        String setusername=prof_name.getText().toString();
        String setuserphone=prof_num.getText().toString();
        String dobs=dob.getText().toString();
        // String Age=

        if(TextUtils.isEmpty(setusername)){
            Toast.makeText(AdminProfileActivity.this,"Please enter your username",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setuserphone)){
            Toast.makeText(AdminProfileActivity.this,"Please enter your status",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(dobs)){
            Toast.makeText(AdminProfileActivity.this,"Please enter your Date of Birth",Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String,Object> profile = new HashMap<>();
            profile.put("uid",currentuserid);
            profile.put("username",setusername);
            profile.put("userphonno",setuserphone);
            profile.put("dob",dobs);

            firedb.child("Admin").updateChildren(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminProfileActivity.this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String errorms=task.getException().toString();
                        Toast.makeText(AdminProfileActivity.this,"Error :"+errorms,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(AdminProfileActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}