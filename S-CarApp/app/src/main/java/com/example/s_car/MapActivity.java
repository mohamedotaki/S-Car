package com.example.s_car;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity {
    double lon =0,lat=0;
    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private final LatLng HOME_LOCATION = new LatLng(53.766258, -8.781946);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuest.start(getApplicationContext());
        setContentView(R.layout.activity_map);


        mMapView = (MapView) findViewById(R.id.mapquestMapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapView.setStreetMode();
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HOME_LOCATION, 14));
                // create and add marker
                CurrentLocation();
            }
        });

    }

    private class Route extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args){
            JSONObject postData = new JSONObject();
            try {
                // JSONArray of start and finish
                JSONArray locations = new JSONArray();
                locations.put(URLEncoder.encode(args[0], "UTF-8"));
                locations.put(URLEncoder.encode(args[1], "UTF-8"));
                postData.put("locations", locations); // put array inside main object

                // JSONObject options
                JSONObject options = new JSONObject();
                options.put("routeType", args[2]);
                options.put("generalize", "0");
                postData.put("options", options);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // create the api request string
            String urlstring = "http://www.mapquestapi.com/directions/v2/route" +
                    "?key=jcOs583N0pN0PpH2NKn9yzh3s4IkZ593&json=" +
                    postData.toString();

            // make the GET request and prep the response string
            StringBuilder json = new StringBuilder();
            try {
                URL url = new URL(urlstring);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        json.append(line);
                    }
                } catch (Exception e) {
                    System.out.println("catch B: " + e.toString());
                } finally {
                    urlConn.disconnect();
                }
            } catch (Exception e) {
                System.out.println("catch C: " + e.toString());
            }
            return json.toString();
        }

        protected void onPostExecute(String json){
            try {
                // get shape points from response
                JSONArray points = new JSONObject(json)
                        .getJSONObject("route")
                        .getJSONObject("shape")
                        .getJSONArray("shapePoints");

                // get every other shape point
                int pointcount = points.length() / 2;

                // create a shape point list
                List<LatLng> shapePoints = new ArrayList<>();

                // fill list with every even value as lat and odd value as lng
                for (int point = 0; point < pointcount; point = point + 1) {
                    shapePoints.add(new LatLng(
                            (double) points.get(point * 2),
                            (double) points.get(point * 2 + 1)
                    ));
                }

                // create polyline options
                PolylineOptions polyline = new PolylineOptions();
                polyline.addAll(shapePoints)
                        .width(5)
                        .color(Color.GRAY)
                        .alpha((float)0.75);

                // add the polyline to the map
                mMapboxMap.addPolyline(polyline);

                // get map bounds
                JSONObject bounds = new JSONObject(json)
                        .getJSONObject("route")
                        .getJSONObject("boundingBox");

                // create bounds for animating map
                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .include(new LatLng(
                                (double) bounds.getJSONObject("ul").get("lat"),
                                (double) bounds.getJSONObject("ul").get("lng")
                        ))
                        .include(new LatLng(
                                (double) bounds.getJSONObject("lr").get("lat"),
                                (double) bounds.getJSONObject("lr").get("lng")
                        ))
                        .build();

                // animate to map bounds
                mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 5000);
            } catch (Exception e) {
                System.out.println("catch D: " + e.toString());
            }
        }
    }

    @Override
    public void onResume()
    { super.onResume(); mMapView.onResume(); }

    @Override
    public void onPause()
    { super.onPause(); mMapView.onPause(); }

    @Override
    protected void onDestroy()
    { super.onDestroy(); mMapView.onDestroy(); }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState); }

    public String CurrentLocation() {
        final List<Address> locationList;
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lon = location.getLongitude();
                            lat = location.getLatitude();
                            LatLng currentLocation = new LatLng(lon,lat);
                            Route route = new Route();
                            route.execute(
                                    lat + ", " + lon, // origin
                                    "12 bracklaghboy, ballyhunis, co.mayo, ireland", // destination
                                    "fastest" // type
                            );
                        }
                    }
                });
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            return lat + ", " + lon;
        }
        return null;
    }


}