package com.example.food;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class FoodDAO{
    //맛집등록
    public static void insert(Food vo, FoodDB foodDB, SQLiteDatabase db){
        String sql="insert into food(name,address,tel,description,latitude,longitude,photo,keep) values(";
        sql += "'"+vo.getName()+"','"+vo.getAddress()+"','"+vo.getTel()+"',";
        sql += "'"+vo.getDescription()+"',"+vo.getLatitude()+","+vo.getLongitude()+",";
        sql += "'"+vo.getPhoto()+"',0)";
        System.out.println("sql............" + sql);
        db.execSQL(sql);
    }

    //즐겨찾기 목록
    public static ArrayList<Food> keepList(FoodDB foodDB, SQLiteDatabase db){
        ArrayList<Food> array=new ArrayList<>();

        String sql="select * from food where keep=1";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Food vo=new Food();
            vo.setSeq(cursor.getInt(cursor.getColumnIndex("seq")));
            vo.setName(cursor.getString(cursor.getColumnIndex("name")));
            vo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            vo.setTel(cursor.getString(cursor.getColumnIndex("tel")));
            vo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            vo.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            vo.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            vo.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
            vo.setKeep(cursor.getInt(cursor.getColumnIndex("keep")));
            array.add(vo);
            System.out.println(vo.toString());
        }
        return array;
    }

    //맛집록록 메서드
    public static ArrayList<Food> list(FoodDB foodDB, SQLiteDatabase db){
        ArrayList<Food> array=new ArrayList<>();

        String sql="select * from food";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            Food vo=new Food();
            vo.setSeq(cursor.getInt(cursor.getColumnIndex("seq")));
            vo.setName(cursor.getString(cursor.getColumnIndex("name")));
            vo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            vo.setTel(cursor.getString(cursor.getColumnIndex("tel")));
            vo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            vo.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            vo.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            vo.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
            vo.setKeep(cursor.getInt(cursor.getColumnIndex("keep")));
            array.add(vo);
            System.out.println(vo.toString());
        }
        return array;
    }

    public static Food read(int seq, FoodDB foodDB, SQLiteDatabase db){
        Food vo=new Food();
        String sql="select * from food where seq=" + seq;
        Cursor cursor=db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            vo.setSeq(cursor.getInt(cursor.getColumnIndex("seq")));
            vo.setName(cursor.getString(cursor.getColumnIndex("name")));
            vo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            vo.setTel(cursor.getString(cursor.getColumnIndex("tel")));
            vo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            vo.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            vo.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            vo.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
            vo.setKeep(cursor.getInt(cursor.getColumnIndex("keep")));
        }
        return vo;
    }

    public static void keepChange(int seq, int keep, FoodDB foodDB, SQLiteDatabase db){
        String sql="update food set keep="+keep + " where seq=" + seq;
        db.execSQL(sql);
    }
}







