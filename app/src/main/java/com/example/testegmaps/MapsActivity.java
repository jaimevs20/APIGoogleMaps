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
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker marker;
    private double latitude;
    private double longitude;
    private Polyline polyline;
    private List<LatLng> lngs;
    private long distancia;

    Coordenada coordenada1 = new Coordenada(-13.873622, -40.071181); //UESB JEE
    Coordenada coordenada2 = new Coordenada(-13.859748, -40.083198); //Banco do Brasil JEE

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
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Seta o tipo do mapa: https://developers.google.com/maps/documentation/android-sdk/map#change_the_map_type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        lngs = new ArrayList<LatLng>();     //Array do caminho

        // Instanciando os locais de acordo às respectivas latitudes e longitudes
        LatLng local1 = new LatLng(coordenada1.getLatitude(), coordenada1.getLongitude());
        LatLng local2 = new LatLng(coordenada2.getLatitude(), coordenada2.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(local1).zoom(10)/*rotação*//*.bearing(0)./*inclinação*/.tilt(90).build();
        CameraUpdate up = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mMap.animateCamera(up, 3000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Log.i("Script", "Fim da animação");   //Ação quando a animação é finalizada
            }

            @Override
            public void onCancel() {
                Log.i("Script", "Animação cancelada");   //Ação quando a animação é cancelada
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

            polylineOptions.color(Color.BLUE);
            polyline = mMap.addPolyline(polylineOptions);   //desenho da rota
        }
        else{
            polyline.setPoints(lngs);
        }
    }

    //Obtém a distância
    public void getDistancia(View view) {
/*        double distancia=0;

        for (int i = 0; i<lngs.size();i++){
            if(i < lngs.size()-1){
                distancia += calculaDistancia(lngs.get(i),lngs.get(i+1));   //Distância total
            }
        }*/
        Toast.makeText(MapsActivity.this,"Distância: "+distancia+" metros",Toast.LENGTH_LONG).show();
    }

    //Calcula a distância entre dois pontos no plano
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

        if(rua == null || cidade == null || pais == null)
            Toast.makeText(MapsActivity.this,"Local não identificado",Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(MapsActivity.this,
                    "Você está em: " + rua+"\nCidade: "+cidade+"\nPaís: "+pais, Toast.LENGTH_LONG).show();
        }
    }

    /*


                            ROTAS PELO GOOGLE MAPS



    */
    //Obtendo rotas inseridas pelo Usuário
    public void getRota(View view) throws IOException, JSONException {
        EditText editTextOrigin = findViewById(R.id.origin);
        EditText editTextDestination = findViewById(R.id.destination);

        //Codificando o que foi digitado nos campos com URLEncoder
        String origin = URLEncoder.encode(editTextOrigin.getText().toString(),"UTF-8") ,
                destination = URLEncoder.encode(editTextDestination.getText().toString(),"UTF-8");

        //obterRota(origin,destination);
        obterRota(new LatLng(coordenada1.getLatitude(),coordenada1.getLongitude()),
                new LatLng(coordenada2.getLatitude(),coordenada2.getLongitude())); // UESB p/ Banco do Brasil
    }

    //Traçando e calculando rota com base no json fornecido pelo Google

    //Origem e Destino Informados pelo Usuário
    //public void obterRota(final String origin, final String destination) throws IOException, JSONException {

    //Por latitude e Longitude (mais preciso):
    public void obterRota(final LatLng origin, final LatLng destination) throws IOException, JSONException {
        String url= /*"https://maps.googleapis.com/maps/api/directions/json?origin="
                +origin.latitude+"&destination="+destination+"&key=AIzaSyABCSds3NYfazat0QP8HADl_bjXLsLmYIA";*/
        "https://maps.googleapis.com/maps/api/directions/json?origin="
                +origin.latitude+","+origin.longitude+"&destination="+destination.latitude+","+destination.longitude
                +"&key=AIzaSyABCSds3NYfazat0QP8HADl_bjXLsLmYIA";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();

                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i("Script", myResponse);
                                lngs = buildJSONRoute(myResponse);
                                desenhaRota();
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    // PARSER JSON - Acesso ao Json gerado pelo Google, e tratamento das informações úteis à aplicação
    public List<LatLng> buildJSONRoute(String json) throws JSONException{
        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        distancia = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");  //Aqui é onde os pontos são capturados
        List<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));


            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }

            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
        }

        return(lines);
    }

    // DECODE POLYLINE
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> listPoints = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            Log.i("Script", "POL: LAT: "+p.latitude+" | LNG: "+p.longitude);
            listPoints.add(p);
        }
        return listPoints;
    }
}
