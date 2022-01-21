package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
TextView tx,tx2;
private FirebaseAuth useraut;
BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tx=findViewById(R.id.texts);
//        tx2=findViewById(R.id.text2);
        useraut= FirebaseAuth.getInstance();
     /*  tx.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,AdminLoginActivity.class));
            }
        });*/

   /*     tx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler=new AlertDialog.Builder(MainActivity.this);
                aler.setTitle("Do you Want to Logout");
                aler.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        useraut.signOut();
                        sendUserToLoginActivity();
                    }
                });
                aler.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                aler.show();
            }
        })*/


        navigationView=findViewById(R.id.Main_bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new ProductFragment()).commit();
        navigationView.setSelectedItemId(R.id.Reedem);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment frag=null;
                switch(item.getItemId())
                {
                    /*case R.id.Home:
                        frag=new AdminHomeFragment();
                        break;*/
                    case R.id.Reedem:
                        frag=new ProductFragment();
                        break;

                    case R.id.Dashboard:
                        frag=new AdminDashBoardFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,frag).commit();
                return true;
            }
        });

    }
    private void sendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this,AdminLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentusers=useraut.getCurrentUser();
        if(currentusers==null){
            sendUserToLoginActivity();
        }

    }
}