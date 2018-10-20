package br.edu.unidavi.mapas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> markers = new ArrayList<>();
    private Button buttonNormal;
    private Button buttonSatelite;
    private Button buttonTerreno;
    private Button buttonMarkerChecker;
    private boolean enableMyLocation = false;
    private LatLng previousPosition = new LatLng(-27.2060804, -49.6452095);

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

        buttonMarkerChecker = findViewById(R.id.marker_checker);
        buttonMarkerChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!markers.isEmpty()) {
                    Marker marker = markers.get(0);
                    if(mMap.getProjection().getVisibleRegion().latLngBounds.contains(marker.getPosition())) {
                        Toast.makeText(MapsActivity.this, "O Marker está na tela!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MapsActivity.this, "O Marker não está na tela!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation = true;
        } else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(MapsActivity.this, "Por Favor, sua localização é necessário", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.setMyLocationEnabled(true);
                } else {
                    enableMyLocation = true;
                }
                drawCircleOnPosition();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void drawCircleOnPosition() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .fillColor(Color.RED)
                        .strokeColor(Color.RED)
                        .radius(25)
                );
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

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style));
        mMap.getUiSettings().setZoomControlsEnabled(true);


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
                    mMap.addPolyline(new PolylineOptions()
                        .add(previousPosition, marker.getPosition())
                        .width(6)
                        .color(Color.RED)
                    );
                    previousPosition = marker.getPosition();
                }
            }
        });
    }
}
