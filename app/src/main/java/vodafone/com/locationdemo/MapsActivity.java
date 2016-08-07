package vodafone.com.locationdemo;

import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        MapsActivityPermissionsDispatcher.getLocationWithCheck(this);
        // Add a marker in Sydney and move the camera

    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public void getLocation() 
    {
        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                apiClientConnected();

            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }).addApi(LocationServices.API).build();

        googleApiClient.connect();

    }

    @SuppressWarnings("MissingPermission")
    public void apiClientConnected()
    {
      Location location=  LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        updateMap(location);
    }

    public void updateMap(Location location)
    {
        LatLng myLatLang=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLatLang).title("My current position"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLang,15));
    }
}
