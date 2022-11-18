package com.example.saferbettersociety;

import static android.view.View.GONE;
import static android.view.View.generateViewId;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.saferbettersociety.databinding.ActivityLandingPageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class Landing_Page extends FragmentActivity implements OnMapReadyCallback {

//initializing variables
    private boolean clicked = false;
    private GoogleMap mMap;
    private ActivityLandingPageBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double lat, lng;
    String full_addres;
    ImageButton add_user, call_contacts, send_sos, call_police, siren;
    Button stop_siren, sms ;
    TextView current_loc, crtv;
    MediaPlayer mp;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        add_user = findViewById(R.id.add_user);
        call_contacts = findViewById(R.id.call_user);
        send_sos = findViewById(R.id.sos);
        call_police = findViewById(R.id.police);
        siren = findViewById(R.id.siren);
        stop_siren = findViewById(R.id.stop);
        current_loc = findViewById(R.id.crnt_loc_tv);
        crtv = findViewById(R.id.crtv);



        stop_siren.setEnabled(false);
        stop_siren.setVisibility(GONE);
        crtv.setVisibility(GONE);



//code for add contacts
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Landing_Page.this, contact_book.class);
                startActivity(i);

            }
        });
//


//code for call contacts
        call_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Landing_Page.this, call_contacts.class);
                startActivity(i);

            }
        });
//


//code for sending notification sos message
        send_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(Landing_Page.this, "MyNotification");
                builder.setContentTitle("I am in trouble");
                builder.setContentText(full_addres);
                builder.setSmallIcon(R.drawable.women_cartoon);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Landing_Page.this);
                managerCompat.notify(1, builder.build());
            }
        });
//





//code for calling police control room
        call_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policePhonecall();
            }
        });
//


        //code for siren
        siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(Landing_Page.this, R.raw.danger);
                stop_siren.setEnabled(true);
                stop_siren.setVisibility(View.VISIBLE);
                siren.setEnabled(false);
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mp.start();
                    }
                });


                stop_siren.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mp.isPlaying()) {
                            mp.pause();
                            siren.setEnabled(true);
                            stop_siren.setEnabled(false);
                            stop_siren.setVisibility(GONE);
                        }
                    }
                });


                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mp.release();
                    }
                });

            }


        });
        //code for siren ends






    }




    private void sendsms() {

        String number = "9353266102";
        String message = "hello how r u";
        if (ContextCompat.checkSelfPermission(Landing_Page.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Landing_Page.this,
                    new String[]{Manifest.permission.SEND_SMS}, 100);
            return;

        } else {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(number, null, message, null, null);
        }

    }

    private void policePhonecall() {

        String number = "100";
        if (ContextCompat.checkSelfPermission(Landing_Page.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Landing_Page.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 99);
            return;

        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
            case 99:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    policePhonecall();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendsms();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;

        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(6000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {


                if (locationResult == null) {
                    Toast.makeText(Landing_Page.this, "current location is null", Toast.LENGTH_SHORT).show();
                }
            }
        };


        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    LatLng latLng = new LatLng(lat, lng);
                   mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));


                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(17)
                            .bearing(90)
                            .tilt(40)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    try {

                        Geocoder geo = new Geocoder(Landing_Page.this,Locale.getDefault());
                        List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                        String address = addresses.get(0).getAddressLine(0);
                        full_addres = address + "\n" + "Latitude - " + lat + "\n" + "Longitude - " +lng;
                        crtv.setVisibility(View.VISIBLE);
                        current_loc.setText(full_addres);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(Landing_Page.this, "Unable to fetch current location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}