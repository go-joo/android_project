package com.example.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
    private static final int MAP_INFO=500;
    Context context;
    ArrayList<Food> arrayMap;
    ArrayList<Float> arrayDistance;

    public MapAdapter(Context context, ArrayList<Food> arrayMap, ArrayList<Float> arrayDistance) {
        this.context = context;
        this.arrayMap = arrayMap;
        this.arrayDistance = arrayDistance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_map,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food vo=arrayMap.get(position);

        Bitmap photo=BitmapFactory.decodeFile(vo.getPhoto());
        if(photo != null) holder.image.setImageBitmap(photo);

        float distance=arrayDistance.get(position)/1000;
        DecimalFormat df=new DecimalFormat("#,##0.00Km");
        String strDistance=df.format(distance);
        holder.distance.setText(strDistance);

        holder.name.setText(vo.getName());
        holder.description.setText(vo.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FoodInfoActivity.class);
                intent.putExtra("seq", vo.getSeq());
                ((Activity)context).startActivityForResult(intent, MAP_INFO);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, description, distance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            distance=itemView.findViewById(R.id.distance);
        }
    }
}
