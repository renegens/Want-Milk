package renegens.wantmilklocator;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements renegens.wantmilklocator.LocationProvider.LocationCallback {

    public static final String TAG2 = MapsActivity.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private LocationProvider mLocationProvider;



    double[] gpsx =
            //For Larisa with pairs x and y and names from array with the same order
            {39.641180, 39.642052, 39.626034, 39.624953, 39.626297, 39.636028, 39.6366,
                    39.640286, 39.633225, 39.642384, 39.632204, 39.631439, 39.63216, 39.650149, 39.6099,
                /*For Thessloniki */
                    40.630591, 40.637867, 40.582404, 40.651851, 40.61476, 40.654166, 40.609253, 40.603881, 40.617316, 40.670667};
    double[] gpsy =
            //For Larisa with pairs x and y and names from array with the same order
            {22.418758, 22.41044, 22.394707, 22.413641, 22.42174, 22.424397, 22.408838,
                    22.426468, 22.41593, 22.439586, 22.402893, 22.423186, 22.441789, 22.432785, 22.431465,
                /*For Thessloniki */
                    22.950781, 22.94882, 22.948664, 22.948162, 22.977683, 22.923416, 22.969673, 22.96865, 22.958589, 22.902774};

    String[] arrayNames;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);


        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent browserIntent = new Intent(MapsActivity.this, Info.class);
                startActivity(browserIntent);

            }
        });
        arrayNames = getResources().getStringArray(R.array.storeArray);

        setUpMapIfNeeded();

        mLocationProvider = new LocationProvider(this, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mLocationProvider.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        //distanceCalculator();
        markerSetUp();

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    public void distanceCalculator(double a, double b) {

        double min = 1000.000000;
        int index = -1;
        //points to compare
        for (int i = 0; i < gpsx.length; i++) {
            double toCompareX = gpsx[i];
            double toCompareY = gpsy[i];
            double result = distFrom(a, b, toCompareX, toCompareY);
            if (result <= min) {
                min = result;
                index = i;
            }
            Log.d("Method ", "index: " + index);
            Log.d("Method ", "result: " + result);
            Log.d("Method ", "min: " + min);
        }
        double closestMarkerX = gpsx[index];
        double closestMarkerY = gpsy[index];
        int newIndex = index;
        Marker closestMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(gpsx[newIndex],gpsy[newIndex]))
                .title(arrayNames[newIndex]));
        closestMarker.showInfoWindow();
        Log.d("Debug", "Index: " + index);
        Log.d("Debug", "X: " + closestMarkerX);
        Log.d("Debug", "Y: " + closestMarkerY);
    }




    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        {
            double theta = lng1 - lng2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }}


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public void handleNewLocation(Location location) {
        Log.d("New location request", location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        distanceCalculator(currentLatitude,currentLongitude);
        /*Camera animation to location*/

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)             // Sets the center of the map to location user
                .zoom(13)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.addMarker(new MarkerOptions().position(latLng).title(getResources().getString(R.string.my_position)).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.markercustom)));
    }

    //Setting up markers from array
    public void markerSetUp() {

        for (int i = 0; i < gpsx.length; i++) {
            double x = gpsx[i];
            double y = gpsy[i];
            MarkerOptions marker = new MarkerOptions().position(new LatLng(x, y)).title(arrayNames[i]);
            mMap.addMarker(marker);
        }

    }

}