package com.example.miniproj2admin.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.miniproj2admin.R;
import com.example.miniproj2admin.models.categoryModel;
import com.example.miniproj2admin.setActivity;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.ViewHolder>{
    private List<categoryModel> cm;
    private deleteListener deleteListener;
    public categoryAdapter(List<categoryModel> cm,deleteListener deleteListener) {

        this.cm = cm;
        this.deleteListener=deleteListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_active_course_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    holder.setData(cm.get(position).getUrl(),cm.get(position).getName(),cm.get(position).getKey(),position,cm.get(position).getCourseDesc());
    }

    @Override
    public int getItemCount() {
        return cm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img;
        private TextView courseName,courseDesc;
        private ImageButton delete;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.cate_img);
            courseName=itemView.findViewById(R.id.Course_Name);
            courseDesc=itemView.findViewById(R.id.Course_desc);
            delete=itemView.findViewById(R.id.delete);

        }
        private void setData(String url,String title,String key,int position,String courseDesc)
        {
            Glide.with(itemView.getContext()).load(url).into(img);
            this.courseName.setText(title);
            this.courseDesc.setText(courseDesc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(itemView.getContext(), setActivity.class);
                    i.putExtra("title",title);
                    i.putExtra("position",position);
                    i.putExtra("key",key);
                    itemView.getContext().startActivity(i);

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onDelete(key,position);
                }
            });

        }

    }

    public interface deleteListener {
        public void onDelete(String key,int position );
    }

}
