package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminLoginActivity extends AppCompatActivity {
    private FirebaseUser currentuser;
    private TextInputEditText useremail,userpassword;
    private Button userlogin,donthaveaccount;
    private ProgressDialog lgnprogress;
    private FirebaseAuth usrauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        userlogin = findViewById(R.id.btlogin);
        useremail = findViewById(R.id.adminemail);
        userpassword = findViewById(R.id.adminpass);
        usrauth=FirebaseAuth.getInstance();
        lgnprogress = new ProgressDialog(this);
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        String UserEmail=useremail.getText().toString();
        String UserPass=userpassword.getText().toString();
        if(TextUtils.isEmpty(UserEmail) && TextUtils.isEmpty(UserPass)){
            Toast.makeText(AdminLoginActivity.this,"Please Enter the Required Credentials",Toast.LENGTH_LONG).show();
        }

        if(!UserEmail.isEmpty() && !UserPass.isEmpty())
        {

            lgnprogress.setTitle("Signing In");
            lgnprogress.setMessage("Please Wait...");
            lgnprogress.setCanceledOnTouchOutside(true);
            lgnprogress.show();
            DatabaseReference dbref= FirebaseDatabase.getInstance().getReference();
            dbref.child("Admin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                    String email=snapshot.child("Email").getValue().toString();
                    String password=snapshot.child("Password").getValue().toString();
                    if(UserEmail.equals(email) && UserPass.equals(password))
                    {
                        usrauth.signInWithEmailAndPassword(UserEmail,UserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    lgnprogress.dismiss();
                                    Toast.makeText(AdminLoginActivity.this,"Logged in Successfully",Toast.LENGTH_LONG).show();
                                    sendUserToMainActivity();
                                    Intent intent= new Intent(AdminLoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    String errormess=task.getException().toString();
                                    Toast.makeText(AdminLoginActivity.this,"Error :"+ errormess,Toast.LENGTH_LONG).show();
                                    lgnprogress.dismiss();
                                }
                            }
                        });

                    }
                    else{
                        lgnprogress.dismiss();
                        Toast.makeText(AdminLoginActivity.this,"Please Enter Correct Credentials",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {

                }
            });
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (currentuser != null) {
            sendUserToMainActivity();
        }
    }
    private void sendUserToMainActivity() {
        Intent mainintent= new Intent(AdminLoginActivity.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }
}