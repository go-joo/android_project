package com.example.food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static int MAIN_PROFILE = 100;
    private final static int MAIN_REGISTER = 200;
    private final static int LIST_INFO = 300;
    private final static int KEEP_INFO=400;
    private final static int MAP_INFO=500;

    DrawerLayout drawerLayout;
    RelativeLayout drawerMain;

    FragmentTransaction tr;
    ArrayList<Food> array;

    FoodDB foodDB;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodDB=new FoodDB(this);
        db=foodDB.getWritableDatabase();
        array=FoodDAO.list(foodDB, db);

        drawerLayout=findViewById(R.id.drawer_layout);
        drawerMain=findViewById(R.id.drawer_main);

        getSupportActionBar().setTitle("맛집리스트");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        tr=getSupportFragmentManager().beginTransaction();
        tr.add(R.id.content_main, new FoodListFragment(array)).commit();

        //네비게이션 메뉴를 선택한경우
        NavigationView nav_menu=findViewById(R.id.nav_menu);
        nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_map:
                        array=FoodDAO.list(foodDB, db);
                        getSupportActionBar().setTitle("지도리스트");
                        tr=getSupportFragmentManager().beginTransaction();
                        tr.replace(R.id.content_main, new FoodMapFragment(array)).commit();
                        break;
                    case R.id.nav_keep:
                        array=FoodDAO.keepList(foodDB, db);
                        getSupportActionBar().setTitle("즐겨찾기");
                        tr=getSupportFragmentManager().beginTransaction();
                        tr.replace(R.id.content_main, new FoodKeepFragment(array)).commit();
                        break;
                    case R.id.nav_list:
                        array=FoodDAO.list(foodDB, db);
                        getSupportActionBar().setTitle("맛집리스트");
                        tr=getSupportFragmentManager().beginTransaction();
                        tr.replace(R.id.content_main, new FoodListFragment(array)).commit();
                        break;
                    case R.id.nav_profile:
                        Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                        startActivityForResult(intent, MAIN_PROFILE);
                        break;
                    case R.id.nav_register:
                        intent=new Intent(MainActivity.this, RegisterActivity.class);
                        startActivityForResult(intent, MAIN_REGISTER);
                        break;
                }
                drawerLayout.closeDrawer(drawerMain);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;
        array=FoodDAO.list(foodDB, db);

        switch (requestCode){
            case MAIN_PROFILE:
                System.out.println("Profile 설정완료.....");
                break;
            case MAIN_REGISTER:
                System.out.println("Register 등록완료....");
                getSupportActionBar().setTitle("맛집리스트");
                tr=getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.content_main, new FoodListFragment(array)).commit();
                break;
            case LIST_INFO:
                System.out.println("FoodInfo 수정완료.....");
                tr=getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.content_main, new FoodListFragment(array)).commit();
                break;
            case KEEP_INFO:
                System.out.println("즐겨찾기-정보.....");
                array=FoodDAO.keepList(foodDB, db);
                tr=getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.content_main, new FoodKeepFragment(array)).commit();
                break;
            case MAP_INFO:
                System.out.println("맵리스트-정보.....");
                tr=getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.content_main, new FoodMapFragment(array)).commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(drawerMain)){
                    drawerLayout.closeDrawer(drawerMain);
                }else{
                    drawerLayout.openDrawer(drawerMain);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}