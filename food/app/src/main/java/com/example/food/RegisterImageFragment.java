package com.example.food;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class RegisterImageFragment extends Fragment {
    private final static int PICK_ALBUM=0;
    private final static int PICK_CAMERA=1;
    String strIcon;
    ImageView image;
    Food food;

    public RegisterImageFragment(Food food) {
        this.food = food;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register_image, container, false);
        image = view.findViewById(R.id.image);
        ImageView camera=view.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] options={"카메라", "앨범"};
                AlertDialog.Builder box=new AlertDialog.Builder(getContext());
                box.setTitle("이미지선택방법");
                box.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                try {
                                    File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                    File file=File.createTempFile("img_", ".jpg", storageDir);
                                    if(file == null) return;
                                    //파일명(/storage/emulated/0/Android/data/패키지명/files/Pictures/파일명)
                                    strIcon = file.getAbsolutePath();
                                    Uri photoURI= FileProvider.getUriForFile(getContext(),
                                            getActivity().getPackageName(), file);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                }catch(Exception e){
                                    System.out.println("에러:" + e.toString());
                                }
                                startActivityForResult(intent, PICK_CAMERA);
                                break;
                            case 1:
                                intent=new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, PICK_ALBUM);
                                break;
                        }
                    }
                });
                box.show();
            }
        });

        System.out.println(".................." + food.toString());

        //완료버튼을 클릭한경우
        Button complete=view.findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder box=new AlertDialog.Builder(getContext());
                box.setTitle("질의");
                box.setMessage("저장하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        food.setPhoto(strIcon);
                        FoodDB foodDB=new FoodDB(getContext());
                        SQLiteDatabase db=foodDB.getWritableDatabase();
                        FoodDAO.insert(food, foodDB, db);
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                });
                box.setNegativeButton("아니오", null);
                box.show();
            }
        });

        //이전버튼을 클릭한경우
        Button prev=view.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).replaceFragment(new RegisterInputFragment(food));
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        switch (requestCode){
            case PICK_ALBUM:
                try{
                    image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                            data.getData()));
                    String[] projection = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(data.getData(), projection, null, null, null);
                    cursor.moveToFirst();
                    //파일명(/storage/emulated/0/Pictures/파일명)
                    strIcon = cursor.getString(cursor.getColumnIndex(projection[0]));
                    cursor.close();
                }catch (Exception e){
                    System.out.println("에러:" + e.toString());
                }
                break;
            case PICK_CAMERA:
                image.setImageBitmap(BitmapFactory.decodeFile(strIcon));
                break;
        }
    }
}