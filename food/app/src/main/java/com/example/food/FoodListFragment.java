package com.example.food;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


public class FoodListFragment extends Fragment {
    RecyclerView list;
    FoodAdapter adapter;
    StaggeredGridLayoutManager layoutManager;
    ArrayList<Food> array;
    int listType = 1;

    public FoodListFragment(ArrayList<Food> array) {
        this.array = array;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_food_list, container, false);

        list=view.findViewById(R.id.list_food);
        adapter = new FoodAdapter(getContext(), array);
        layoutManager=new StaggeredGridLayoutManager(listType, StaggeredGridLayoutManager.VERTICAL);
        list.setAdapter(adapter);
        list.setLayoutManager(layoutManager);

        final ImageView imgType=view.findViewById(R.id.list_type);
        imgType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listType==1){
                    listType=2;
                    imgType.setImageResource(R.drawable.ic_list2);
                }else{
                    listType=1;
                    imgType.setImageResource(R.drawable.ic_list);
                }
                layoutManager=new StaggeredGridLayoutManager(listType, StaggeredGridLayoutManager.VERTICAL);
                list.setLayoutManager(layoutManager);
            }

        });

        return view;
    }
}