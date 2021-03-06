package com.example.miniproj2admin.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miniproj2admin.QuestionsActivity;
import com.example.miniproj2admin.R;

import java.util.List;


public class gridAdapter extends BaseAdapter {

    public List<String> sets;
    private String category;
    private GridListener listener;

    public gridAdapter(List<String> sets, String category,GridListener listener) {
        this.sets = sets;
        this.category=category;
        this.listener=listener;
    }


    @Override
    public int getCount() {
        return sets.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item,parent,false);
        }
        else
        {
            view=convertView;

        }
        if(position==0)
        {
            ((TextView)view.findViewById(R.id.textBVew)).setText("+");

        }
        else
        {
            ((TextView)view.findViewById(R.id.textBVew)).setText(String.valueOf(position));

        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0)
                {
                    listener.addSet();
                }
                else
                {
                    Intent q=new Intent(parent.getContext(), QuestionsActivity.class);
                    q.putExtra("category",category);
                    q.putExtra("setId",sets.get(position -1));
                    parent.getContext().startActivity(q);
                }


            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(position!=0)
                {
                    listener.onLongClick(sets.get(position-1),position);
                }

                return false;
            }
        });
        return view;
    }
    public interface GridListener
    {
        public void addSet();
        void onLongClick(String setId,int position);
    }
}
