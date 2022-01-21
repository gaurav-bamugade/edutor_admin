package com.example.miniproj2admin.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.miniproj2admin.CategoryActivity;
import com.example.miniproj2admin.ProdDetailsActivity;
import com.example.miniproj2admin.R;
import com.example.miniproj2admin.editProdDetailsActivity;
import com.example.miniproj2admin.models.productsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class productItemAdapter extends RecyclerView.Adapter<productItemAdapter.Viewholder> {

    Context context;
    List<productsModel> prodm;
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference mr=dat.getReference();


    public productItemAdapter(Context context, List<productsModel> prodm) {
        this.context = context;
        this.prodm = prodm;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.prod_item_card,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        /*holder.prodQty.setText(prodm.get(position).getProductQty());*/

        Picasso.get().load(prodm.get(position).getImageUrl()).into(holder.prodImage);
        holder.prodName.setText(prodm.get(position).getProductName());
        holder.prodPrice.setText(String.valueOf(prodm.get(position).getProductPrice()));
        holder.setData( position);
    }

    @Override
    public int getItemCount() {
        return prodm.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView prodImage,editOpts;
        TextView prodName,prodPrice,prodCate;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        /*    prodQty =itemView.findViewById(R.id.prod_qty);*/
            prodImage =itemView.findViewById(R.id.prod_img);
            prodName =itemView.findViewById(R.id.prod_name);
            prodPrice=itemView.findViewById(R.id.prod_coins);
            prodCate=itemView.findViewById(R.id.prod_category);
            editOpts=itemView.findViewById(R.id.editOpts);




        }
        private void setData(int post)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(itemView.getContext(), ProdDetailsActivity.class);
                    i.putExtra("prodid",prodm.get(post).getProductId());
                    itemView.getContext().startActivity(i);

                }
            });

            editOpts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(context,R.style.Theme_AppCompat_Light_Dialog)
                            .setTitle("Products")
                            .setMessage("Please choose and option!!")
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i=new Intent(itemView.getContext(), editProdDetailsActivity.class);
                                    i.putExtra("prodid",prodm.get(post).getProductId());
                                    itemView.getContext().startActivity(i);
                                }
                            })
                            .setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mr.child("products").child(prodm.get(post).getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(context,"Removed Product Successfully", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                            else
                                            {
                                                Toast.makeText(context,"Failed to delete", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();




                }
            });
        }
    }
}
