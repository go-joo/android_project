package com.example.food;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterInputFragment extends Fragment {
    TextView txtCount;
    Food food;
    EditText name, address, tel, description;
    public RegisterInputFragment(Food food) {
        this.food = food;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register_input, container, false);

        address=view.findViewById(R.id.address);
        address.setText(food.getAddress());
        name=view.findViewById(R.id.name);
        tel=view.findViewById(R.id.tel);
        description=view.findViewById(R.id.description);

        //다음버튼
        Button next=view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setName(name.getText().toString());
                food.setTel(tel.getText().toString());
                food.setDescription(description.getText().toString());
                food.setAddress(address.getText().toString());
                ((RegisterActivity)getActivity()).replaceFragment(new RegisterImageFragment(food));
            }
        });

        //이전버튼
        Button prev=view.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).replaceFragment(new RegisterLocationFragment(food));
            }
        });

        //맛집정보 글자수카운트
        txtCount = view.findViewById(R.id.txtCount);
        final EditText description=view.findViewById(R.id.description);
        description.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int length = description.getText().toString().length();
                txtCount.setText(String.valueOf(length));
                if(length == 500)
                    Toast.makeText(getContext(), "10자를 넘을수 없습니다!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return view;
    }
}