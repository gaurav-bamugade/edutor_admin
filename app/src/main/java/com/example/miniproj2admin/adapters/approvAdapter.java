package com.example.miniproj2admin.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproj2admin.ProdDetailsActivity;
import com.example.miniproj2admin.R;
import com.example.miniproj2admin.editProdDetailsActivity;
import com.example.miniproj2admin.models.approvModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class approvAdapter extends RecyclerView.Adapter<approvAdapter.ViewHolder> {

    List<approvModel> ram;
    Context context;
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference mr=dat.getReference();

    public approvAdapter(List<approvModel> ram, Context context) {
        this.ram = ram;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public approvAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.approv_card_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull approvAdapter.ViewHolder holder, int position) {
        holder.approvusrname.setText(ram.get(position).getUserName());
        holder.approvusremail.setText(ram.get(position).getUserEmail());
        holder.prodname.setText(ram.get(position).getProduct_name());
        holder.prodprice.setText(ram.get(position).getProduct_price());
        Picasso.get().load(ram.get(position).getProduct_image_url()).into(holder.prodimg);
        holder.approvwpending.setText(ram.get(position).getApproval());
        holder.setData( position);
    }
    @Override
    public int getItemCount() {
        return ram.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prodname,prodprice,approvusrname,approvusremail,approvwpending;
        CircleImageView prodimg;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            prodname=itemView.findViewById(R.id.approv_prod_name);
            prodprice=itemView.findViewById(R.id.approv_prod_price);
            approvusrname=itemView.findViewById(R.id.approv_user_name);
            approvusremail=itemView.findViewById(R.id.approv_user_email);
            prodimg=itemView.findViewById(R.id.approv_prod_img);
            approvwpending=itemView.findViewById(R.id.approv_pending);

        }
        private void setData(int post)
        {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(context,R.style.Theme_AppCompat_Light_Dialog)
                            .setTitle("Approval")
                            .setMessage("Please choose and option!!")
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mr.child("redeems").child(ram.get(post).getRedeemId()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            HashMap<String , Object> hash=new HashMap<>();
                                            hash.put("approval","Your Product Has Been Approved By Admin!!");
                                            hash.put("iconimg","https://firebasestorage.googleapis.com/v0/b/miniproj2-cddb9.appspot.com/o/greenCorrect.png?alt=media&token=a0d28935-fc90-41ca-8b10-afb7d903beb6");
                                            hash.put("pleaseWait","Congratulations!!!");
                                            mr.child("redeems").child(ram.get(post).getRedeemId()).updateChildren(hash);
                                            mr.child("products").child(ram.get(post).getProd_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                   int currentstockcount=Integer.parseInt(snapshot.child("stock_count").getValue().toString());
                                                   int updatedStockCount=currentstockcount-1;
                                                    HashMap<String,Object> stockctn=new HashMap<>();
                                                    stockctn.put("stock_count",updatedStockCount);

                                                    mr.child("products").child(ram.get(post).getProd_id()).updateChildren(stockctn);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                            })
                            .setNeutralButton("DisApprove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mr.child("redeems").child(ram.get(post).getRedeemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            mr.child("products").child(ram.get(post).getProd_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot prodsnapshot)
                                                {

                                                    mr.child("users").child(ram.get(post).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot usrsnapshot) {
                                                            int prodcoins=Integer.parseInt(prodsnapshot.child("product_price").getValue().toString());
                                                            int currentcoins=Integer.parseInt(usrsnapshot.child("coins").getValue().toString());

                                                            int updatedStockCount=currentcoins+prodcoins;

                                                            HashMap<String,Object> stockctn=new HashMap<>();
                                                            stockctn.put("coins",updatedStockCount);

                                                            mr.child("users").child(ram.get(post).getUid()).updateChildren(stockctn);

                                                            HashMap<String , Object> hashupd=new HashMap<>();
                                                            hashupd.put("approval","Your Approval Has Been Denied!!");
                                                            hashupd.put("iconimg","https://firebasestorage.googleapis.com/v0/b/miniproj2-cddb9.appspot.com/o/remove.png?alt=media&token=2f5bb0c2-427a-40f8-8411-04cf98881214");
                                                            hashupd.put("pleaseWait","Sorry!!!");
                                                            mr.child("redeems").child(ram.get(post).getRedeemId()).updateChildren(hashupd);
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                        }
                                                    });


                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });



                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

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
