package com.example.testegmaps;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double latitude, longitude, latitude2, longitude2;
    private Marker marker;
    private Polyline polyline;
    private List<LatLng> lngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
/*
        //Prepara o mapa para a utilização
        GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);

        //Setando o Fragmento do mapa via API
        mapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        /*

        //Desenha o Mapa através do Fragmento
        fragmentTransaction.replace(R.id.map, mapFragment);
        fragmentTransaction.commit();
        */

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //Desenha o Mapa através do Fragmento
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        //Seta o tipo do mapa: https://developers.google.com/maps/documentation/android-sdk/map#change_the_map_type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        latitude = -22.9035;
        longitude = -43.2096;
        latitude2 = -22.9095;
        longitude2 = -43.2086;

        // Instanciando os locais de acordo às respectivas latitudes e longitudes
        LatLng local1 = new LatLng(latitude, longitude);
        LatLng local2 = new LatLng(latitude2, longitude2);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(local1).zoom(15)/*rotação*//*.bearing(0)./*inclinação*/.tilt(90).build();
        CameraUpdate up = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mMap.animateCamera(up, 3000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Log.i("Script", "CancelableCallback.onFinish()");   //Ação quando a animação é finalizada
            }

            @Override
            public void onCancel() {
                Log.i("Script", "CancelableCallback.onCancel()");   //Ação quando a animação é cancelada
            }
        });

        //Markers (Marcadores)
        addMarker(local1, "Marker do Local Inicial", "Descrição");
        //addMarker(local2, "Marker do Local 2", "Descrição");

        //EVENTOS
        //Clique no mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng local) {
                if(marker != null){
                    marker.remove();
                }
                //adiciona um novo marcador no local do clique
                addMarker(new LatLng(local.latitude,local.longitude), "Marker Reposicionado", "Lat: "+local.latitude+" Long: "+local.longitude);
            }
        });

        //Clique no Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        //Clique na Janelinha de Descrição
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this,"Clicou na Janela", Toast.LENGTH_LONG).show();
            }
        });


        /*
        //Adiciona o Marcador na Localização Indicada
        mMap.addMarker(new MarkerOptions().position(local).title("Marker no Local"));
        //Permite a interação com a "câmera" do mapa (zoom)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(local));
        mMap.setBuildingsEnabled(true);

        //Habilita Mapas de Ambientes Internos
        //mMap.setIndoorEnabled(true);*/
    }

    //Cria um Marker
    public void addMarker(LatLng local, String title, String snippet){
        Marker marker = mMap.addMarker(new MarkerOptions().position(local).title(title).snippet(snippet).draggable(true));
    }

}
