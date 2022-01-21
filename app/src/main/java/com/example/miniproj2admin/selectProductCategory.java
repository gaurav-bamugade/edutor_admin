package com.example.miniproj2admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class selectProductCategory extends AppCompatActivity {
Toolbar toolbar;
LinearLayout arts,books,pens,bags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product_category);

        toolbar=findViewById(R.id.toolbrcate);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Product Category");
        arts=findViewById(R.id.art);
        books=findViewById(R.id.books);
        pens=findViewById(R.id.pens);
        bags=findViewById(R.id.bags);

        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arts();
            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               books();
            }
        });
        pens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pens();
            }
        });
        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bags();
            }
        });

    }

    private void arts()
    {
        Intent i=new Intent(selectProductCategory.this,newProductUploadActivity.class);
        i.putExtra("cate","Arts");
        startActivity(i);
    }
    private void pens()
    {
        Intent i=new Intent(selectProductCategory.this,newProductUploadActivity.class);
        i.putExtra("cate","Pens");
        startActivity(i);
    }
    private void books()
    {
        Intent i=new Intent(selectProductCategory.this,newProductUploadActivity.class);
        i.putExtra("cate","Books");
        startActivity(i);
    }

    private void bags()
    {
        Intent i=new Intent(selectProductCategory.this,newProductUploadActivity.class);
        i.putExtra("cate","Bags");
        startActivity(i);
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