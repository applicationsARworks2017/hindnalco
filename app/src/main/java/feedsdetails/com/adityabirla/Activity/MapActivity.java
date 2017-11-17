package feedsdetails.com.adityabirla.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import feedsdetails.com.adityabirla.R;

public class MapActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    GoogleMap googleMap;
    SupportMapFragment fm;
    private String provider;
    Geocoder geocoder;
    MarkerOptions markerOptions;
    String address,city,state;
    String latitude="21.526891",longitude="83.896472";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Getting reference to SupportMapFragment of the activity_main
        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.self_map);

        // Getting Map for the SupportMapFragment
        googleMap = fm.getMap();






       /* double lat = location.getLatitude();
        double lng = location.getLongitude();
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));*/
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        LatLng latLng = new LatLng(lat, lng);
        // for getting the address
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address=addresses.get(0).getAddressLine(0);
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }


        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(address+","+city+","+state);
        googleMap.addMarker(markerOptions);
        googleMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
