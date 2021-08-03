package com.example.food;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


public class RegisterLocationFragment extends Fragment implements OnMapReadyCallback {
    ProgressBar progressBar;
    private GoogleMap mMap;
    TextView txtAddress;
    Marker moveMarker;
    Food food;

    public RegisterLocationFragment(Food food) {
        this.food = food;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_location, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        txtAddress = view.findViewById(R.id.address);

        Button next=view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).replaceFragment(new RegisterInputFragment(food));
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

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                double latitude=latLng.latitude;
                double longitude=latLng.longitude;

                MarkerOptions marker=new MarkerOptions();
                marker.position(latLng);
                marker.draggable(true);
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                if(moveMarker != null) moveMarker.remove();
                moveMarker = mMap.addMarker(marker);

                String address=getAddress(getContext(), latitude, longitude);
                txtAddress.setText(address);

                food.setLongitude(longitude);
                food.setLatitude(latitude);
                food.setAddress(address);
            }
        });
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

            //String address = (getAddress(getContext(), latitude, longitude));
            //txtAddress.setText(address);
            progressBar.setVisibility(View.GONE);
        }
    };

    public static String getAddress(Context context, double latitude, double longitude) {
        String strAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
            strAddress = address.get(0).getAddressLine(0).toString();

        } catch (Exception e) { }
        return strAddress;
    }
}