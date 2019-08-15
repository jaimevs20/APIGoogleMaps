package com.example.testegmaps;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Prepara o mapa para a utilização
        GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);

        //Setando o Fragmento do mapa via API
        mapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        /***/

        //Desenha o Mapa através do Fragmento
        fragmentTransaction.replace(R.id.map, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
        /***/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //Desenha o Mapa através do Fragmento
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        latitude = -34;
        longitude = 151;
        // Add a marker in Sydney and move the camera
        LatLng local = new LatLng(latitude, longitude);
        //Adiciona o Marcador na Localização Indicada
        mMap.addMarker(new MarkerOptions().position(local).title("Marker no Local"));
        //Permite a interação com a "câmera" do mapa (zoom)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        //Seta o tipo do mapa: https://developers.google.com/maps/documentation/android-sdk/map#change_the_map_type
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Habilita Mapas de Ambientes Internos
        //mMap.setIndoorEnabled(true);
    }
/*
    public void onResume() {

        super.onResume();

        new Thread() {
            public void run() {
                while (mapFragment == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
*/
}
