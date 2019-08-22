/**
 * GPS e funcionalidade que atualiza a localização do Usuário
 */

package com.example.testegmaps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.testegmaps.permissoes.GerenciarPermissoes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class GPSMapsActivity2 extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener/*, GoogleMap.OnMyLocationButtonClickListener*/{

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmaps2);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        obterLocalizacao();

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

        /*Toast.makeText(this, "Latitude atual: " + local.latitude+"\n"+
                "Logitude atual:"+this.local.longitude, Toast.LENGTH_LONG).show();*/
       // mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }

    public void obterLocalizacao(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location!=null){
                    zoomNaLocalizacaoAtual(location);
                }
            }
        });
    }

    public void toastLocalizacaoAtual(){
        Toast.makeText(this, "Latitude atual: " + local.latitude+"\n"+
                "Logitude atual:"+this.local.longitude, Toast.LENGTH_LONG).show();
    }

    public void zoomNaLocalizacaoAtual(Location location){
        local = new LatLng(location.getLatitude(),location.getLongitude());     //Localização atual

        CameraPosition cameraPosition = new CameraPosition.Builder().target(local).zoom(15).build();
        CameraUpdate up = CameraUpdateFactory.newCameraPosition(cameraPosition);

        toastLocalizacaoAtual();
        mMap.animateCamera(up);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        LatLng local1 = new LatLng(location.getLatitude(),location.getLongitude());     //Local Atual

        CameraPosition cameraPosition = new CameraPosition.Builder().target(local1).zoom(20).tilt(75).build();
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
