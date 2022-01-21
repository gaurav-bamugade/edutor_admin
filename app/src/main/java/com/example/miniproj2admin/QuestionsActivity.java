package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproj2admin.adapters.questionsAdapter;
import com.example.miniproj2admin.models.questionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestionsActivity extends AppCompatActivity {
private Button add_btn,excel_btn;
private RecyclerView quest_rc;
private questionsAdapter adapter;
private Dialog loading;
public static List<questionsModel> list;
private DatabaseReference myref;
public static final int cells_count=6;
private int set;
private String categnmae;
private TextView loadingText;
private String setId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        myref=  FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categnmae=getIntent().getStringExtra("category");
        setId=getIntent().getStringExtra("setId");

        /*getSupportActionBar().setTitle(categnmae+"/set"+set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        add_btn=findViewById(R.id.add_btn);
        loading=new Dialog(this);
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loading.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loading.setCancelable(false);
        loadingText=loading.findViewById(R.id.textviewLoading);

        excel_btn=findViewById(R.id.excel_btn);

        list=new ArrayList<>();

        quest_rc=findViewById(R.id.quest_rc);

        LinearLayoutManager ln=new LinearLayoutManager(this);
        ln.setOrientation(RecyclerView.VERTICAL);
        quest_rc.setLayoutManager(ln);

        adapter=new questionsAdapter(list, categnmae, new questionsAdapter.DeleteListener() {
            @Override
            public void onLongClick(int position, String id) {
                new AlertDialog.Builder(QuestionsActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                        .setTitle("Delete Question")
                        .setMessage("Are you Sure , You want to delete this quesiton?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loading.show();
                                myref.child("SETS").child(setId).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            list.remove(position);
                                            adapter.notifyItemRemoved(position);
                                        }
                                        else
                                        {
                                            Toast.makeText(QuestionsActivity.this,"Failed to delete", Toast.LENGTH_SHORT).show();

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
        quest_rc.setAdapter(adapter);

        getData(categnmae,setId);


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(QuestionsActivity.this,AddQuestionActivity.class);
               i.putExtra("categoryName",categnmae);
               i.putExtra("setId",setId);
                startActivity(i);
            }
        });

        excel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(QuestionsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectFile();
                }
                else
                {
                    ActivityCompat.requestPermissions(QuestionsActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);


                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101 )
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                selectFile();
            }
            else
            {
                Toast.makeText(this,"please grant the permission",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void selectFile()
    {
        Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.setType("*/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(i,"select file"),102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==102)
        {
            if(resultCode==RESULT_OK)
            {
                String filePath=data.getData().getPath();
                if(filePath.endsWith(".xlsx"))
                {
                    readFile(data.getData());
                }
                else
                {
                    Toast.makeText(this,"please choose excel file",Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    private void readFile(Uri filepath) {
        loadingText.setText("Scanning Questions...");
        loading.show();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


        HashMap<String , Object> parentmap=new HashMap<>();
        List<questionsModel> templist=new ArrayList<>();

        try
        {
            InputStream inputStream=getContentResolver().openInputStream(filepath);
            XSSFWorkbook workbook=new XSSFWorkbook(inputStream);
            XSSFSheet sheets=workbook.getSheetAt(0);
            FormulaEvaluator formulaEvaluator=workbook.getCreationHelper().createFormulaEvaluator();

            int rowscount=sheets.getPhysicalNumberOfRows();
            if(rowscount>0)
            {
                for(int r=0;r<rowscount;r++)
                {
                    Row row=sheets.getRow(r);
                   if(row.getPhysicalNumberOfCells()==cells_count)
                   {
                       String question=getCellData(row,0,formulaEvaluator);
                       String a=getCellData(row,1,formulaEvaluator);
                       String b=getCellData(row,2,formulaEvaluator);
                       String c=getCellData(row,3,formulaEvaluator);
                       String d=getCellData(row,4,formulaEvaluator);
                       String correctAns=getCellData(row,5,formulaEvaluator);


                       /*if(correctAns.equals(a) || correctAns.equals(b) || correctAns.equals(c) || correctAns.equals(d))
                       {*/

                           String id= UUID.randomUUID().toString();
                           HashMap<String,Object> questionMap=new HashMap<>();
                           questionMap.put("correctAns",correctAns);
                           questionMap.put("id",id);
                           questionMap.put("optionA",a);
                           questionMap.put("optionB",b);
                           questionMap.put("optionC",c);
                           questionMap.put("optionD",d);
                           questionMap.put("question",question);
                           questionMap.put("set",set);
                           parentmap.put(id,questionMap);
                           templist.add(new questionsModel(id,question,a,b,c,d,correctAns,setId));

                      /* }*/
                      /* else
                       {
                           int finalR1 = r;
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   loadingText.setText("Loading...");
                                   loading.dismiss();
                                   Toast.makeText(QuestionsActivity.this,"Row no"+(finalR1 +1)+"has not correct ans",Toast.LENGTH_SHORT).show();
                               }
                           });
                           return;
                       }*/
                   }
                   else
                   {
                       int finalR = r;
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               loadingText.setText("Loading...");
                               loading.dismiss();
                               Toast.makeText(QuestionsActivity.this,"Row no"+(finalR +1)+"has incoorect data",Toast.LENGTH_SHORT).show();

                           }
                       });
                       return;

                   }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingText.setText("Uploading...");
                        FirebaseDatabase.getInstance().getReference()
                                .child("SETS").child(setId).updateChildren(parentmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    list.addAll(templist);
                                    adapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    loadingText.setText("Loading...");
                                    Toast.makeText(QuestionsActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();

                                }
                                loading.dismiss();

                            }
                        });

                    }
                });

            }
            else
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingText.setText("Loading...");
                        loading.dismiss();
                        Toast.makeText(QuestionsActivity.this,"file is empty",Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingText.setText("Loading...");
                    loading.dismiss();
                    Toast.makeText(QuestionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (IOException e)
        {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingText.setText("Loading...");
                    loading.dismiss();
                    Toast.makeText(QuestionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

            }
        });
    }

    private String getCellData(Row row,int cellPosition,FormulaEvaluator formulaEvaluator)
    {
        String value="";
        Cell cell=row.getCell(cellPosition);

        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_BOOLEAN:
                return value+cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return value+cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return value+cell.getStringCellValue();

            default:
                return value;



        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(String categnmae, String setId)
    {
        loading.show();
        myref.child("SETS").child(setId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                for(DataSnapshot ds:snapshot.getChildren())
                {

                    String id=ds.child("id").getValue().toString();;
                    String quesiton=ds.child("question").getValue().toString();
                    String a=ds.child("optionA").getValue().toString();
                    String b=ds.child("optionB").getValue().toString();
                    String c=ds.child("optionC").getValue().toString();
                    String d=ds.child("optionD").getValue().toString();
                    String correctAns=ds.child("correctAns").getValue().toString();
                    list.add(new questionsModel(id,quesiton,a,b,c,d,correctAns,setId));
                    Log.d("check", id.toString());
                    Log.d("check", quesiton.toString());
                    Log.d("check", a.toString());
                    Log.d("check", b.toString());
                    Log.d("check", c.toString());
                    Log.d("check", d.toString());
                    Log.d("check", correctAns.toString());

                }
                loading.dismiss();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

}