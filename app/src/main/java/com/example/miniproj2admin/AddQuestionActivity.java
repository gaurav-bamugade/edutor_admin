package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.miniproj2admin.models.questionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AddQuestionActivity extends AppCompatActivity {
private EditText questionssss;
private RadioGroup options;
private LinearLayout answers;
private Button uploadBtn;
private Dialog loading;
private String Categoryname;
private int position;
private String setid;
private questionsModel questionModel;
private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        Categoryname=getIntent().getStringExtra("categoryName");
        setid= getIntent().getStringExtra("setId");
        position= getIntent().getIntExtra("position",-1);
        uploadBtn=findViewById(R.id.add_question_btn);
        questionssss=findViewById(R.id.add_question);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        options=findViewById(R.id.options);
        answers=findViewById(R.id.answersss);

        if(setid==null)
        {
            finish();
            return;
        }
        if(position!=-1)
        {   questionModel =QuestionsActivity.list.get(position);

            Log.d("thisss", String.valueOf((int)position));
            setData();
        }

        loading=new Dialog(this);
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loading.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loading.setCancelable(false);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(questionssss.getText().toString().isEmpty())
                {
                    questionssss.setError("Required");
                    return;
                }
                upload();
            }
        });

    }
    private void setData()
    {

        questionssss.setText(questionModel.getQuestion());

        ((EditText)answers.getChildAt(0)).setText(questionModel.getA());
        ((EditText)answers.getChildAt(1)).setText(questionModel.getB());
        ((EditText)answers.getChildAt(2)).setText(questionModel.getC());
        ((EditText)answers.getChildAt(3)).setText(questionModel.getD());

        for(int i=0;i<answers.getChildCount();i++)
        {
            if(((EditText)answers.getChildAt(i)).getText().toString().equals(questionModel.getAnswer()));
            {
                RadioButton radioButton=(RadioButton)options.getChildAt(i);
                radioButton.setChecked(true);
                break;
            }
        }
    }
    private void upload()
    {
        int correct =-1;
        HashMap<String,Object> hashMp=new HashMap<>();
        for(int i=0;i<options.getChildCount();i++)
        {

            EditText ans= (EditText) answers.getChildAt(i);
            if(ans.getText().toString().isEmpty())
            {
                ans.setError("Required");
                return;
            }

            RadioButton radioButton=(RadioButton)options.getChildAt(i);
            if(radioButton.isChecked())
            {
                correct=i;
                break;

            }
        }
        if(correct==-1)
        {
            Toast.makeText(this,"please Mark the correct option",Toast.LENGTH_SHORT).show();
            return;
        }

        if(position!=-1)
        {
            id=questionModel.getId();

        }
        else
        {
           id =UUID.randomUUID().toString();
        }
        hashMp.put("id",id);
        hashMp.put("correctAns",((EditText)answers.getChildAt(correct)).getText().toString());
        hashMp.put("optionA",((EditText)answers.getChildAt(3)).getText().toString());
        hashMp.put("optionB",((EditText)answers.getChildAt(2)).getText().toString());
        hashMp.put("optionC",((EditText)answers.getChildAt(1)).getText().toString());
        hashMp.put("optionD",((EditText)answers.getChildAt(0)).getText().toString());
        hashMp.put("question",questionssss.getText().toString());
        hashMp.put("set",setid);

        loading.show();
        FirebaseDatabase.getInstance().getReference()
                .child("SETS").child(setid).child(id)
                .updateChildren(hashMp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    questionsModel  questionsModel=new questionsModel(id,hashMp.get("question").toString()
                            ,hashMp.get("optionA").toString(),hashMp.get("optionB").toString(),hashMp.get("optionC").toString(),hashMp.get("optionD").toString()
                            ,hashMp.get("correctAns").toString(),hashMp.get("setId").toString());

                   if(position!=-1)
                   {
                       QuestionsActivity.list.set(position,questionsModel);

                   }
                   else
                   {
                       QuestionsActivity.list.add(questionsModel);

                   }
                    finish();
                }
                else
                {
                    Toast.makeText(AddQuestionActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
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

}

     /*String a=questionModel.getA().toString();
        String b=questionModel.getA().toString();
        String c=questionModel.getA().toString();
        String d=questionModel.getA().toString();*/
/*
       Log.d("strings",a.toString());
        Log.d("strings",b.toString());  Log.d("strings",c.toString());  Log.d("strings",d.toString());*/