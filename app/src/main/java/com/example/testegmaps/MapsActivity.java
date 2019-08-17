package com.example.testegmaps;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.icu.text.CaseMap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        lngs = new ArrayList<LatLng>();

        latitude = -22.9035;
        longitude = -43.2096;
        latitude2 = -22.9095;
        longitude2 = -43.2086;

        // Instanciando os locais de acordo às respectivas latitudes e longitudes
        LatLng local1 = new LatLng(latitude, longitude);
        LatLng local2 = new LatLng(latitude2, longitude2);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(local1).zoom(20)/*rotação*//*.bearing(0)./*inclinação*/.tilt(90).build();
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
       // addMarker(local1, "Marker do Local Inicial", "Descrição");
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
                addMarker(new LatLng(local.latitude,local.longitude), "Marker posicionado", "Lat: "+local.latitude+" Long: "+local.longitude);
                lngs.add(local);    //preenche o array com a rota
                desenhaRota();      //desenha as rotas
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
        /*Marker*/ marker = mMap.addMarker(new MarkerOptions().position(local).title(title).snippet(snippet).draggable(true));
    }

    public void desenhaRota(){
        PolylineOptions polylineOptions;
        int tam = lngs.size();

        if(polyline == null){
            polylineOptions = new PolylineOptions();

            for(int i = 0; i < tam; i++){
                polylineOptions.add(lngs.get(i));   //preenchendo o array list
            }
            polylineOptions.color(Color.GREEN);
            polyline = mMap.addPolyline(polylineOptions);   //desenho da rota
        }
        else{
            polyline.setPoints(lngs);
        }
    }

    //Obtém a distância
    public void getDistancia(View view) {
        double distancia=0;

        for (int i = 0; i<lngs.size();i++){
            if(i < lngs.size()-1){
                distancia += calculaDistancia(lngs.get(i),lngs.get(i+1));   //Distância total
            }
        }
        Toast.makeText(MapsActivity.this,"Distância: "+distancia+" metros",Toast.LENGTH_LONG).show();
    }

    //Calcula a distância entre dois pontos
    public static double calculaDistancia(LatLng local1, LatLng local2){
       //Método mais preciso e utilizado pelos desenvolvedores no cálculo das distâncias
        double lat1 = local1.latitude;
        double lat2 = local2.latitude;
        double lon1 = local1.longitude;
        double lon2 = local2.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
       /*   Location localOrigem = new Location(LocationManager.GPS_PROVIDER),
                localDestino = new Location(LocationManager.GPS_PROVIDER);

        localOrigem.setLatitude(local1.latitude);
        localOrigem.setLongitude(local2.longitude);

        localDestino.setLatitude(local2.latitude);
        localDestino.setLatitude(local2.longitude);

        return localOrigem.distanceTo(localDestino);*/
       /* if (local1 == null || local2 == null)
            return Double.parseDouble(null);
        float[] result = new float[1];
        Location.distanceBetween(local1.latitude, local2.longitude,
                local1.latitude, local1.latitude, result);
        return (double) result[0];*/
    }

    //Captura a localização do Marker no mapa
    public void getLocal(View view) throws IOException {
        Geocoder geocoder = new Geocoder(MapsActivity.this);

        //retorna uma lista com informações do endereço
        List<Address> endereco = geocoder.getFromLocation
                (lngs.get(lngs.size() - 1).latitude,lngs.get(lngs.size() - 1).longitude,1);

        String rua = endereco.get(0).getThoroughfare(); //retorna o nome da rua
        String cidade = endereco.get(0).getSubAdminArea(); //retorna o nome da cidade
        String pais = endereco.get(0).getCountryName(); //retorna o nome do país

        Toast.makeText(MapsActivity.this,"Você está em: "+rua,Toast.LENGTH_LONG).show();
    }
}
