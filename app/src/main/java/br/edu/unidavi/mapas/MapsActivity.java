package br.edu.unidavi.mapas;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> markers = new ArrayList<>();
    private Button buttonNormal;
    private Button buttonSatelite;
    private Button buttonTerreno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonNormal = findViewById(R.id.type_map_normal);
        buttonSatelite = findViewById(R.id.type_map_satelite);
        buttonTerreno = findViewById(R.id.type_map_terreno);

        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        buttonSatelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        buttonTerreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
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
        addDragListener();
        addToolTipListener();

        // Add a marker in Sydney and move the camera
        LatLng unidavi = new LatLng(-27.2060804, -49.6452095);

        LatLng unidavi2 = new LatLng(-27.2060601, -49.6452560);

        MarkerOptions marker = new MarkerOptions().draggable(true).position(unidavi).title("Marker in UNIDAVI")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        MarkerOptions marker2 = new MarkerOptions().draggable(true).position(unidavi2).title("Marker in UNIDAVI 2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name));

        markers.add(mMap.addMarker(marker));
        //markers.add(mMap.addMarker(marker2));
        mMap.addMarker(marker2);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(unidavi));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17f));
    }

    private void addToolTipListener() {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                markers.remove(marker);
                marker.remove();
                Toast.makeText(MapsActivity.this, "Marker Removido!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDragListener() {
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if(markers.contains(marker)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                }
            }
        });
    }
}
