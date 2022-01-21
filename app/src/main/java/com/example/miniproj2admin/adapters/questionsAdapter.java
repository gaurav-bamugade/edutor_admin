package com.example.miniproj2admin.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproj2admin.AddQuestionActivity;
import com.example.miniproj2admin.R;
import com.example.miniproj2admin.models.questionsModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class questionsAdapter extends RecyclerView.Adapter<questionsAdapter.ViewHolder> {
 private List<questionsModel> list;
private String category;
private DeleteListener listener;


    public questionsAdapter(List<questionsModel> list,String category,DeleteListener listener) {
        this.category=category;
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_items,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull questionsAdapter.ViewHolder holder, int position) {

        String question = list.get(position).getQuestion();
        String Answer=list.get(position).getAnswer();

        holder.setData(question,Answer,position);
    }

    @Override
    public int getItemCount() {
       return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView question,answer;
        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            question=itemView.findViewById(R.id.questions);
            answer=itemView.findViewById(R.id.answers);

        }

        private void setData(String question,String answer,int position)
        {
            this.question.setText(position+1+","+question);
            this.answer.setText("Ans. "+answer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(itemView.getContext(), AddQuestionActivity.class);
                    i.putExtra("categoryName",category);
                    i.putExtra("setId",list.get(position).getSet());
                    i.putExtra("position",position);
                    itemView.getContext().startActivity(i);

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(position,list.get(position).getId());
                    return false;
                }
            });


        }

    }
    public interface DeleteListener
    {
        void onLongClick(int position,String id);
    }
}
