package com.example.miniproj2admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashBoardFragment extends Fragment {
private LinearLayout btn;
private FirebaseAuth useraut;

private CardView approvals,uploadprods,settings,addCourse,logout;
    public AdminDashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_dash_board, container, false);

        approvals=v.findViewById(R.id.admin_approvals);
        uploadprods=v.findViewById(R.id.upload_prods);
        settings=v.findViewById(R.id.admin_Settings);
        addCourse=v.findViewById(R.id.Add_course);
        useraut= FirebaseAuth.getInstance();
        logout=v.findViewById(R.id.admin_LogOut);
        approvals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvalsActivity();
            }
        });

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseActivity();
            }
        });
        uploadprods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadprodsActivity();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsActivity();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutActivity();
            }
        });


        return v;
    }


    private void approvalsActivity() {
        Intent intent = new Intent(getContext(),ApprovalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    private void uploadprodsActivity() {
        Intent intent = new Intent(getContext(),selectProductCategory.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    private void settingsActivity() {
        Intent intent = new Intent(getContext(),AdminProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


    private void addCourseActivity() {
        Intent intent = new Intent(getContext(),CategoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    private void logoutActivity() {
        AlertDialog.Builder aler=new AlertDialog.Builder(getContext());
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
    private void sendUserToLoginActivity() {
        Intent intent = new Intent(getContext(),AdminLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}