package com.example.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FoodInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ArrayList<Food> array;
    Food vo;
    int seq;
    FoodDB foodDB;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setTitle("맛집정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        seq = intent.getIntExtra("seq", 0);

        foodDB=new FoodDB(this);
        db=foodDB.getWritableDatabase();
        vo=FoodDAO.read(seq, foodDB, db);

        ImageView image=findViewById(R.id.image);
        Bitmap photo= BitmapFactory.decodeFile(vo.getPhoto());
        if(photo != null) image.setImageBitmap(photo);

        final ImageView keep=findViewById(R.id.keep);
        if(vo.getKeep()==1){
            keep.setImageResource(R.drawable.ic_keep_on);
        }else{
            keep.setImageResource(R.drawable.ic_keep_off);
        }
        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vo.getKeep()==1){
                    AlertDialog.Builder box=new AlertDialog.Builder(FoodInfoActivity.this);
                    box.setTitle("즐겨찾기 삭제");
                    box.setMessage("즐겨찾기에 삭제하실래요?");
                    box.setNegativeButton("아니오", null);
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            keep.setImageResource(R.drawable.ic_keep_off);
                            vo.setKeep(0);
                            FoodDAO.keepChange(seq, vo.getKeep(), foodDB,db);
                        }
                    });
                    box.show();
                }else{
                    AlertDialog.Builder box=new AlertDialog.Builder(FoodInfoActivity.this);
                    box.setTitle("즐겨찾기 추가");
                    box.setMessage("즐겨찾기에 추가하실래요?");
                    box.setNegativeButton("아니오", null);
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            keep.setImageResource(R.drawable.ic_keep_on);
                            vo.setKeep(1);
                            FoodDAO.keepChange(seq, vo.getKeep(), foodDB,db);
                        }
                    });
                    box.show();
                }
            }
        });

        TextView name=findViewById(R.id.name);
        name.setText(vo.getName());

        TextView tel=findViewById(R.id.tel);
        tel.setText(vo.getTel());
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri phone= Uri.parse("tel:" + vo.getTel());
                Intent telIntent=new Intent(Intent.ACTION_DIAL, phone);
                startActivity(telIntent);
            }
        });

        TextView description = findViewById(R.id.description);
        description.setText(vo.getDescription());

        TextView address=findViewById(R.id.address);
        address.setText(vo.getAddress());

        TextView location=findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng=new LatLng(vo.getLatitude(), vo.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(vo.getName()+"\n"+vo.getTel()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent=new Intent();
                intent.putExtra("array", array);
                setResult(RESULT_OK, intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings settings=mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);

        LatLng sydney = new LatLng(vo.getLatitude(), vo.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title(vo.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
    }
}