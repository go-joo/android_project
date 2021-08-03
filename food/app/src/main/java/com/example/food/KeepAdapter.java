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

public class KeepAdapter extends RecyclerView.Adapter<KeepAdapter.ViewHolder> {
    private final static int KEEP_INFO=400;
    ArrayList<Food> array;
    Context context;
    FoodDB foodDB;
    SQLiteDatabase db;

    public KeepAdapter(ArrayList<Food> array, Context context) {
        this.array = array;
        this.context = context;
        foodDB = new FoodDB(context);
        db=foodDB.getWritableDatabase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_food,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Food vo=array.get(position);

        Bitmap photo = BitmapFactory.decodeFile(vo.getPhoto());
        if(photo != null) holder.image.setImageBitmap(photo);

        holder.name.setText(vo.getName());
        holder.description.setText(vo.getDescription());
        holder.keep.setImageResource(R.drawable.ic_keep_on);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FoodInfoActivity.class);
                intent.putExtra("seq", vo.getSeq());
                ((Activity)context).startActivityForResult(intent, KEEP_INFO);
            }
        });

        holder.keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder box = new AlertDialog.Builder(context);
                box.setTitle("즐겨찾기 삭제");
                box.setMessage("즐겨찾기에 삭제하실래요");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.keep.setImageResource(R.drawable.ic_keep_off);
                        vo.setKeep(0);
                        FoodDAO.keepChange(vo.getSeq(),vo.getKeep(),foodDB,db);
                        array.remove(vo);
                        notifyDataSetChanged();
                    }
                });
                box.setNegativeButton("아니오" ,null);
                box.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, keep;
        TextView name, description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            keep=itemView.findViewById(R.id.keep);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
        }
    }
}
