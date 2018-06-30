package com.for20games.posicionamientogps;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    Double latitudCasa=58.55;
    Double longitudCasa=68.66;

    //Posicion del trabajo
    Double latitudTrabajo;
    Double longitudTrabajo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void guardarPosicionCasa() {
        SharedPreferences preferencias = getSharedPreferences("PosicionamientoCasa", Context.MODE_PRIVATE);

        try {
            String posicionString = preferencias.getString("posicion", "[]");
            JSONArray posicion = new JSONArray(posicionString);

            JSONObject object = new JSONObject();

            object.remove("latitud");
            object.remove("longitud");
            object.put("latitud", latitudCasa);
            object.put("longitud", longitudCasa);

            posicion.put(object);

            preferencias.edit().putString("posicion", posicion.toString()).apply();
        } catch (JSONException error) {
            error.printStackTrace();
        }
    }
    private void guardarPosicionTrabajo() {
        SharedPreferences preferencias = getSharedPreferences("PosicionamientoTrabajo", Context.MODE_PRIVATE);

        try {
            String posicionString = preferencias.getString("posicion", "[]");
            JSONArray posicion = new JSONArray(posicionString);

            JSONObject object = new JSONObject();

            object.remove("latitud");
            object.remove("longitud");
            object.put("latitud", latitudTrabajo);
            object.put("longitud", longitudTrabajo);

            posicion.put(object);

            preferencias.edit().putString("posicion", posicion.toString()).apply();
        } catch (JSONException error) {
            error.printStackTrace();
        }
    }
    private void guardarPosicionFacultad() {
        SharedPreferences preferencias = getSharedPreferences("PosicionamientoFacultad", Context.MODE_PRIVATE);

        try {
            String posicionString = preferencias.getString("posicion", "[]");
            JSONArray posicion = new JSONArray(posicionString);

            JSONObject object = new JSONObject();

            object.remove("latitud");
            object.remove("longitud");
            object.put("latitud", latitudFacultad);
            object.put("longitud", longitudFacultad);

            posicion.put(object);

            preferencias.edit().putString("posicion", posicion.toString()).apply();
        } catch (JSONException error) {
            error.printStackTrace();
        }
    }

}
