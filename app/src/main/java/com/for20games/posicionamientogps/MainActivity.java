package com.for20games.posicionamientogps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.Manifest.permission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private LocationCallback locationCallback;

    //Posicion actual
    Double latitudActual;
    Double longitudActual;

    //Posicion de la facultad
    Double latitudFacultad;
    Double longitudFacultad;

    //Posicion de casa
    Double latitudCasa;
    Double longitudCasa;

    //Posicion del trabajo
    Double latitudTrabajo;
    Double longitudTrabajo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView testo = findViewById(R.id.TESTO);
        client = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //usamos locationResult.getLastLocation() para tener
                //la ubicación más reciente
                Location lastLocation = locationResult.getLastLocation();
                longitudActual = lastLocation.getLongitude();
                latitudActual = lastLocation.getLatitude();

                testo.setText(String.valueOf(longitudActual));
                testo.append(" ");
                testo.append(String.valueOf(latitudActual));

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!chequearPermisosLocalizacion()) {
            pedirPermisosLocalizacion();
        } else {
            obtenerLocalizacion();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario concedió el permiso, ya podemos usar la ubicación
                obtenerLocalizacion();
            }
        }
    }

    private boolean chequearPermisosLocalizacion() {
        int permissionState = ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisosLocalizacion() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{permission.ACCESS_FINE_LOCATION},
                123);
    }

    @SuppressWarnings("MissingPermission")
    public void obtenerLocalizacion() {
        //Acá usamos el método de geolocalización que hayamos elegido
        LocationRequest request = LocationRequest.create()
                .setInterval(5*1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client.requestLocationUpdates(request,  locationCallback, null);

    }

}
