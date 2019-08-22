/**
 * GPS e funcionalidade que atualiza a localização do Usuário
 */

package com.example.testegmaps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.testegmaps.permissoes.GerenciarPermissoes;
import com.example.testegmaps.permissoes.PermitirLocalizacao;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class GPSMapsActivity2 extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener/*, GoogleMap.OnMyLocationButtonClickListener*/{

    private GoogleMap mMap;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmaps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        GerenciarPermissoes gerenciarPermissoes = new GerenciarPermissoes();
        gerenciarPermissoes.setActivity(this);
        gerenciarPermissoes.setContext(this);
        gerenciarPermissoes.setmMap(mMap);
        gerenciarPermissoes.habilitarMinhaLocalizacao();

        LatLng local = new LatLng(-13.858821, -40.081640);     //Jequié
        CameraPosition cameraPosition = new CameraPosition.Builder().target(local).zoom(15).build();
        CameraUpdate up = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mMap.animateCamera(up);

       // mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        LatLng local1 = new LatLng(location.getLatitude(),location.getLongitude());     //Local Atual
        CameraPosition cameraPosition = new CameraPosition.Builder().target(local1).zoom(15).build();
        CameraUpdate up = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mMap.animateCamera(up);

        Toast.makeText(this, "Latitude atual: " + location.getLatitude()+"\n"+
                "Logitude atual:"+location.getLongitude(), Toast.LENGTH_LONG).show();
    }
/*
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }*/
}
