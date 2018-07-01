package com.for20games.posicionamientogps;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.Manifest.permission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    Double latitudCasa;
    Double longitudCasa;

    //Posicion del trabajo
    Double latitudTrabajo;
    Double longitudTrabajo;

    boolean eligioPosicionTrabajo=false;
    boolean eligioPosicionCasa=false;
    boolean eligioPosicionFacultad=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView posActual = findViewById(R.id.posicionActual);
        final TextView casa = findViewById(R.id.casa);
        final TextView facultad = findViewById(R.id.facultad);
        final TextView trabajo = findViewById(R.id.trabajo);

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


                posActual.setText("Ubicacion actual:  \n");
                posActual.append(String.valueOf(longitudActual));
                posActual.append("\n");
                posActual.append(String.valueOf(latitudActual));



                Location locAct = new Location("");
                locAct.setLatitude(latitudActual);
                locAct.setLongitude(longitudActual);

                if (eligioPosicionCasa) {

                    Location locCasa = new Location("");
                    locCasa.setLatitude(latitudCasa);
                    locCasa.setLongitude(longitudCasa);

                    casa.setText("Estas a ");
                    casa.append(String.valueOf(locAct.distanceTo(locCasa)));
                    casa.append(" metros");

                    double metros = locAct.distanceTo(locCasa);

                    if (metros > 500.00) {
                        casa.setBackgroundColor(Color.rgb(255, 00, 00));
                    } else if (metros == 0) {
                        casa.setBackgroundColor(Color.rgb(0, 255, 0));
                    } else {
                        int rojo = 255 * ((int) metros) / 500;
                        int verde = 255 - rojo;
                        casa.setBackgroundColor(Color.rgb(rojo, verde, 0));

                    }


                }

                if (eligioPosicionFacultad) {

                    Location locFacultad = new Location("");
                    locFacultad.setLatitude(latitudFacultad);
                    locFacultad.setLongitude(longitudFacultad);

                    facultad.setText("Estas a ");
                    facultad.append(String.valueOf(locAct.distanceTo(locFacultad)));
                    facultad.append(" metros");
                    double metros = locAct.distanceTo(locFacultad);

                    if (metros > 500.00) {
                        facultad.setBackgroundColor(Color.rgb(255, 00, 00));
                    } else if (metros == 0) {
                        facultad.setBackgroundColor(Color.rgb(0, 255, 0));
                    } else {
                        int rojo = 255 * ((int) metros) / 500;
                        int verde = 255 - rojo;
                        facultad.setBackgroundColor(Color.rgb(rojo, verde, 0));

                    }

                }

                if (eligioPosicionTrabajo) {

                    Location locTrabajo = new Location("");
                    locTrabajo.setLatitude(latitudTrabajo);
                    locTrabajo.setLongitude(longitudTrabajo);

                    trabajo.setText("Estas a ");
                    trabajo.append(String.valueOf(locAct.distanceTo(locTrabajo)));
                    trabajo.append(" metros");
                    double metros = locAct.distanceTo(locTrabajo);

                    if (metros > 500.00) {
                        trabajo.setBackgroundColor(Color.rgb(255, 00, 00));
                    } else if (metros == 0) {
                        trabajo.setBackgroundColor(Color.rgb(0, 255, 0));
                    } else {
                        int rojo = 255 * ((int) metros) / 500;
                        int verde = 255 - rojo;
                        trabajo.setBackgroundColor(Color.rgb(rojo, verde, 0));

                    }

                }


            }
        };
    }
    private void restaurarPosiciones() {

        SharedPreferences casa = getSharedPreferences("PosicionamientoCasa", Context.MODE_PRIVATE);
        SharedPreferences trabajo = getSharedPreferences("PosicionamientoTrabajo", Context.MODE_PRIVATE);
        SharedPreferences facultad = getSharedPreferences("PosicionamientoFacultad", Context.MODE_PRIVATE);

        String arregloCasa = casa.getString("posicion", " ");
        String arregloTrabajo = trabajo.getString("posicion", " ");
        String arregloFacultad = facultad.getString("posicion", " ");

        if (arregloCasa != " "){
            try {
            JSONArray arregloC = new JSONArray(arregloCasa);
            JSONObject posicion = (JSONObject) arregloC.get(0);
            eligioPosicionCasa = posicion.getBoolean("eligio");

            latitudCasa = posicion.getDouble("latitud");

            longitudCasa = posicion.getDouble("longitud");

              } catch (JSONException e) {
              e.printStackTrace();
             }
         }

         if (arregloTrabajo != " "){
            try {
                JSONArray arreglo = new JSONArray(arregloTrabajo);
                JSONObject posicion = (JSONObject) arreglo.get(0);
                eligioPosicionTrabajo = posicion.getBoolean("eligio");

                latitudTrabajo = posicion.getDouble("latitud");

                longitudTrabajo = posicion.getDouble("longitud");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (arregloFacultad != " ") {
            try {
                JSONArray arreglo = new JSONArray(arregloFacultad);
                JSONObject posicion = (JSONObject) arreglo.get(0);
                eligioPosicionFacultad = posicion.getBoolean("eligio");

                latitudFacultad = posicion.getDouble("latitud");

                longitudFacultad = posicion.getDouble("longitud");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!chequearPermisosLocalizacion()) {
            pedirPermisosLocalizacion();
        } else {
            obtenerLocalizacion();
            restaurarPosiciones();
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

    public void setLocalization(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.botonTrabajo:
                latitudTrabajo = latitudActual;
                longitudTrabajo = longitudActual;
                eligioPosicionTrabajo = true;
                guardarPosicionTrabajo();
                break;
            case R.id.botonCasa:
                latitudCasa = latitudActual;
                longitudCasa = longitudActual;
                eligioPosicionCasa = true;
                guardarPosicionCasa();
                break;
            case R.id.botonFacultad:
                latitudFacultad = latitudActual;
                longitudFacultad = longitudActual;
                eligioPosicionFacultad = true;
                guardarPosicionFacultad();
                break;
        }
    }

    private void guardarPosicionCasa( ) {
        SharedPreferences preferencias = getSharedPreferences("PosicionamientoCasa", Context.MODE_PRIVATE);
        preferencias.edit().clear().commit();
        try {
            String posicionString = preferencias.getString("posicion", "[]");
            JSONArray posicion = new JSONArray(posicionString);

            JSONObject object = new JSONObject();

            object.put("eligio", true);
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
        preferencias.edit().clear().commit();

        try {
            String posicionString = preferencias.getString("posicion", "[]");
            JSONArray posicion = new JSONArray(posicionString);

            JSONObject object = new JSONObject();

            object.put("eligio", true);
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
        preferencias.edit().clear().commit();

        try {
            String posicionString = preferencias.getString("posicion", "[]");
            JSONArray posicion = new JSONArray(posicionString);

            JSONObject object = new JSONObject();

            object.put("eligio", true);
            object.put("latitud", latitudFacultad);
            object.put("longitud", longitudFacultad);

            posicion.put(object);

            preferencias.edit().putString("posicion", posicion.toString()).apply();
        } catch (JSONException error) {
            error.printStackTrace();
        }
    }

    protected void onSaveInstanceState(Bundle guardar) {
        super.onSaveInstanceState(guardar);
        guardar.putDouble("latitud casa", latitudCasa);
        guardar.putDouble("latitud trabajo", latitudTrabajo);
        guardar.putDouble("latitud facultad", latitudFacultad);
        guardar.putDouble("longitud casa", longitudCasa);
        guardar.putDouble("longitud trabajo", longitudTrabajo);
        guardar.putDouble("longitud facultad", longitudFacultad);
    }

    protected void onRestoreInstanceState(Bundle cargar) {
        super.onRestoreInstanceState(cargar);
        latitudCasa = cargar.getDouble("latitud casa");
        latitudTrabajo= cargar.getDouble("latitud trabajo");
        latitudFacultad = cargar.getDouble("latitud facultad");
        longitudCasa = cargar.getDouble("longitud casa");
        longitudTrabajo = cargar.getDouble("longitud trabajo");
        longitudFacultad = cargar.getDouble("longitud facultad");

    }
}
