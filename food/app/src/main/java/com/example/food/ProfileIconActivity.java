package com.example.food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileIconActivity extends AppCompatActivity {
    private static final int PICK_CAMERA = 0;
    private static final int PICK_ALBUM = 1;
    String strIcon;
    CircleImageView imgIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_icon);

        getSupportActionBar().setTitle("프로필 이미지 변경");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgIcon=findViewById(R.id.profile_icon);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.submit:
                //이미지변경프로그램밍
                Intent intent=new Intent();
                intent.putExtra("strIcon", strIcon);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void mClick(View view){
        switch (view.getId()){
            case R.id.album:
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_ALBUM);
                break;
            case R.id.camera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file=File.createTempFile("img_", ".jpg", storageDir);
                    if(file == null) return;
                    //파일명(/storage/emulated/0/Android/data/패키지명/files/Pictures/파일명)
                    strIcon = file.getAbsolutePath();
                    Uri photoURI= FileProvider.getUriForFile(this, getPackageName(), file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }catch(Exception e){
                    System.out.println("에러:" + e.toString());
                }
                startActivityForResult(intent, PICK_CAMERA);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case PICK_ALBUM:
                try{
                    imgIcon.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()));
                    String[] projection = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(data.getData(), projection, null, null, null);
                    cursor.moveToFirst();
                    //파일명(/storage/emulated/0/Pictures/파일명)
                    strIcon = cursor.getString(cursor.getColumnIndex(projection[0]));
                    cursor.close();
                }catch (Exception e){
                    System.out.println("에러:" + e.toString());
                }
                break;
            case PICK_CAMERA:
                imgIcon.setImageBitmap(BitmapFactory.decodeFile(strIcon));
                break;
        }
    }
}