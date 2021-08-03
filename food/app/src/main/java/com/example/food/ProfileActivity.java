package com.example.food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    EditText edtGender, edtBirthday;
    private final static int PRO_ICON=100;
    CircleImageView imgIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgIcon = findViewById(R.id.ic_person);
        getSupportActionBar().setTitle("프로필 설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtGender=findViewById(R.id.profile_gender);
        edtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] genders={"여자", "남자"};
                AlertDialog.Builder box=new AlertDialog.Builder(ProfileActivity.this);
                box.setTitle("성별선택");
                box.setItems(genders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtGender.setText(genders[which]);
                    }
                });
                box.show();
            }
        });

        edtBirthday = findViewById(R.id.profile_birthday);
        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar cal=new GregorianCalendar();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day= cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog box=new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String strBirthday=String.format("%04d-%02d-%02d", year, month+1,dayOfMonth);
                                edtBirthday.setText(strBirthday);
                            }
                        }, year, month, day);
                box.show();
            }
        });

        ImageView imgChangeIcon=findViewById(R.id.profile_icon_change);
        imgChangeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, ProfileIconActivity.class);
                startActivityForResult(intent, PRO_ICON);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK) return;
        if(requestCode == PRO_ICON){
            String strIcon=(String)data.getStringExtra("strIcon");
            imgIcon.setImageBitmap(BitmapFactory.decodeFile(strIcon));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
            case R.id.submit:
                //프로필 설정저장
                setResult(RESULT_OK);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}