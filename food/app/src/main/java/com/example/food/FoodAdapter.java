package com.example.food;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private static final int LIST_INFO = 300;
    Context context;
    ArrayList<Food> array;
    FoodDB foodDB;
    SQLiteDatabase db;

    public FoodAdapter(Context context, ArrayList<Food> array) {
        this.context = context;
        this.array = array;

        foodDB = new FoodDB(context);
        db=foodDB.getWritableDatabase();
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_food,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodAdapter.ViewHolder holder, int position) {
        final Food vo=array.get(position);


        Bitmap photo= BitmapFactory.decodeFile(vo.getPhoto());
        if(photo!=null) holder.image.setImageBitmap(photo);

        holder.name.setText(vo.getName());
        holder.description.setText(vo.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FoodInfoActivity.class);
                intent.putExtra("seq", vo.getSeq());
                ((Activity)context).startActivityForResult(intent, LIST_INFO);
            }
        });

        if(vo.getKeep()==1){
            holder.keep.setImageResource(R.drawable.ic_keep_on);
        }else{
            holder.keep.setImageResource(R.drawable.ic_keep_off);
        }
        holder.keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vo.getKeep()==1) {
                    AlertDialog.Builder box = new AlertDialog.Builder(context);
                    box.setTitle("즐겨찾기 삭제");
                    box.setMessage("즐겨찾기에 삭제하실래요");
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            holder.keep.setImageResource(R.drawable.ic_keep_off);
                            vo.setKeep(0);
                            FoodDAO.keepChange(vo.getSeq(),vo.getKeep(),foodDB,db);
                        }
                    });
                    box.setNegativeButton("아니오" ,null);
                    box.show();
                }else{
                    AlertDialog.Builder box = new AlertDialog.Builder(context);
                    box.setTitle("즐겨찾기 추가");
                    box.setMessage("즐겨찾기에 추가 하실래요");
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            holder.keep.setImageResource(R.drawable.ic_keep_on);
                            vo.setKeep(1);
                            FoodDAO.keepChange(vo.getSeq(),vo.getKeep(),foodDB,db);
                        }
                    });
                    box.setNegativeButton("아니오" ,null);
                    box.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, keep;
        TextView name, tel, description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            keep = itemView.findViewById(R.id.keep);
            name= itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
        }
    }
}
