package edu.dartmouth.cs.battleroyalego;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {

    Location user_location;
    double user_location_lat, user_location_long;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://hackdartmo.firebaseio.com").getReference();
    GeoFire geoFire = new GeoFire(ref);
    List<String> nearestGames = Arrays.asList(new String[5]);
    String gameID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        user_location = getIntent().getParcelableExtra("USER_LOCATION");
        final String userUID = getIntent().getStringExtra("USER_UID");
        user_location_lat = user_location.getLatitude();
        user_location_long = user_location.getLongitude();
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(user_location_lat, user_location_long), 1);
        final Button game1 = findViewById(R.id.game1);
        final Button game2 = findViewById(R.id.game2);
        final Button game3 = findViewById(R.id.game3);
        final Button game4 = findViewById(R.id.game4);
        final Button game5 = findViewById(R.id.game5);


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            int i = 0;
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println("found a game!");
                if (i < 5) {
                    nearestGames.set(i, key);
                    i++;
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                game1.setText(nearestGames.get(0));
                game2.setText(nearestGames.get(1));
                game3.setText(nearestGames.get(2));
                game4.setText(nearestGames.get(3));
                game5.setText(nearestGames.get(4));
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);

        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, GameRoom.class);
                intent.putExtra("USER_LOCATION", user_location);
                intent.putExtra("GAME_NAME", game1.getText());
                gameID = game1.getText().toString();
                startActivity(intent);
            }
        });

        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, GameRoom.class);
                intent.putExtra("USER_LOCATION", user_location);
                intent.putExtra("GAME_NAME", game2.getText());
                gameID = game2.getText().toString();
                startActivity(intent);
            }
        });

        game3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, GameRoom.class);
                intent.putExtra("USER_LOCATION", user_location);
                intent.putExtra("GAME_NAME", game3.getText());
                gameID = game3.getText().toString();
                startActivity(intent);
            }
        });

        game4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, GameRoom.class);
                intent.putExtra("USER_LOCATION", user_location);
                intent.putExtra("GAME_NAME", game4.getText());
                gameID = game4.getText().toString();
                startActivity(intent);
            }
        });

        game5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, GameRoom.class);
                intent.putExtra("USER_LOCATION", user_location);
                intent.putExtra("GAME_NAME", game5.getText());
                gameID = game5.getText().toString();
                startActivity(intent);
            }
        });

        String url = getString(R.string.game_url) + "/" + gameID + "/join/" + userUID;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // what to do on response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_location_lat", Double.toString(user_location_lat));
                params.put("user_location_long", Double.toString(user_location_long));
                return params;
            }
        });

        queue.add(postRequest);

    }
}
