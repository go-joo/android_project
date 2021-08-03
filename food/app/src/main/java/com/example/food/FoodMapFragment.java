package com.example.food;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FoodMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    ArrayList<Food> array;
    ProgressBar progressBar;

    MapAdapter adapter;
    RecyclerView list;

    public FoodMapFragment(ArrayList<Food> array) {
        this.array = array;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_food_map, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        list = view.findViewById(R.id.list);
        final Button listType= view.findViewById(R.id.list_type);
        listType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.getVisibility()==View.VISIBLE){
                    list.setVisibility(View.GONE);
                    listType.setText("목록보기");
                }else{
                    list.setVisibility(View.VISIBLE);
                    listType.setText("목록닫기");
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings settings=mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);

        LocationManager locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        String finePermission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ActivityCompat.checkSelfPermission(getContext(), finePermission) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, gpsLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, gpsLocationListener);
    }

    LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            mMap.addMarker(new MarkerOptions().position(latLng).title("현재위치")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker)));

            CircleOptions circleOptions = new CircleOptions().center(latLng)
                    .radius(500)
                    .fillColor(0x440000FF)
                    .strokeColor(0x110000FF)
                    .strokeWidth(4);
            mMap.addCircle(circleOptions);

            //반경500미터안의 맛집 표시
            ArrayList<Food> arrayMarker=new ArrayList<>();
            ArrayList<Float> arrayDistance=new ArrayList<>();
            for(Food food:array){
                float[] result=new float[1];
                Location.distanceBetween(latitude, longitude, food.getLatitude(), food.getLongitude(), result);
                if(result[0] <= 500){
                    arrayMarker.add(food);
                    arrayDistance.add(result[0]);
                    LatLng markerLocation=new LatLng(food.getLatitude(), food.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(markerLocation).title(food.getName()));
                }
            }

            adapter =new MapAdapter(getContext(), arrayMarker, arrayDistance);
            list.setAdapter(adapter);
            list.setLayoutManager(new LinearLayoutManager(getContext()));
            progressBar.setVisibility(View.GONE);
        }
    };
}