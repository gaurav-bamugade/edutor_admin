package com.example.miniproj2admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.miniproj2admin.adapters.productItemAdapter;
import com.example.miniproj2admin.models.productsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {

private EditText search;
private RecyclerView prod_cr;
    List<productsModel> list,searchModel;
    productItemAdapter proditemAda;
    private DatabaseReference myref;

    public ProductFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_product, container, false);
        search=v.findViewById(R.id.search_prod);

        prod_cr=v.findViewById(R.id.prod_recycler);

        myref=  FirebaseDatabase.getInstance().getReference();
        list=new ArrayList<>();
        getData();
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getContext());
        prod_cr.setLayoutManager(lm);
        proditemAda=new productItemAdapter(getContext(),list);
        prod_cr.setAdapter(proditemAda);
        searchModel=new ArrayList<>();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                searchModel.clear();
                if(s.toString().isEmpty())
                {
                    proditemAda=new productItemAdapter(getContext(),list);
                    prod_cr.setAdapter(proditemAda);
                    proditemAda.notifyDataSetChanged();
                }
                else
                {
                    Filter(s.toString());
                }

            }


        });
        return v;
    }

    private void getData()
    {

        myref.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot ds:snapshot.getChildren())
                {

                    String prodid=ds.child("prod_id").getValue().toString();;
                    String prodDesc=ds.child("product_description").getValue().toString();
                    String prodName=ds.child("product_name").getValue().toString();
                    String prodPrice=ds.child("product_price").getValue().toString();
                    String stockCount=ds.child("stock_count").getValue().toString();
                    String prodCate=ds.child("category").getValue().toString();
                    String prodimg=ds.child("product_image_url").getValue().toString();
                    list.add(new productsModel(prodid,prodName,Integer.parseInt(stockCount),Integer.parseInt(prodPrice),prodimg,prodCate,prodDesc));
                }
                proditemAda.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void Filter(String text)
    {
        for(productsModel ps: list)
        {
            if(ps.getProductName().equals(text))
            {
                searchModel.add(ps);
            }
        }
        proditemAda=new productItemAdapter(getContext(),searchModel);
        prod_cr.setAdapter(proditemAda);
        proditemAda.notifyDataSetChanged();
    }
}