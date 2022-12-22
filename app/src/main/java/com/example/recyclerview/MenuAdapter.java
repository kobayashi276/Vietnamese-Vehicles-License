package com.example.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<menu_class> listPerson;
    private Context mContext;

    public MenuAdapter(Context context, ArrayList<menu_class> listPerson){
        this.mContext = context;
        this.listPerson = listPerson;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        menu_class p = listPerson.get(position);
        if (p==null){
            return;
        }
        holder.rvPic.setImageResource(p.getPic());
        holder.rvFullName.setText(p.getFullname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                int values = 0;
                if (p.getFullname()=="Lí thuyết") {
                    values = 1;
                }else if (p.getFullname()=="Biển báo") {
                    values = 2;
                }else if (p.getFullname()=="Câu hỏi liệt") {
                    values = 3;
                }
                else if (p.getFullname()=="Thi thử"){
                    values = 4;
                }

                Intent intent = new Intent(view.getContext(),ThiThu.class);
                intent.putExtra("idcategories",values);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        if(listPerson!=null){
            return listPerson.size();
        }
        return 0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView rvPic;
        TextView rvFullName;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            rvPic = itemView.findViewById(R.id.product_image);
            rvFullName = itemView.findViewById(R.id.product_title);

        };
    }

    private static final int REQUEST_CODE_QUESTION = 1;

//    private void localCategories(){
//        Database database = new Database(this);
//
//    }
}


