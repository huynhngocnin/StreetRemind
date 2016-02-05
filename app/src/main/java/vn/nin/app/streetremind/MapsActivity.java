package vn.nin.app.streetremind;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import vn.nin.app.streetremind.Module.Fab;
import vn.nin.app.streetremind.database.DatabaseHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap = null;
    private Database database = null;

    private MaterialSheetFab materialSheetFab;

    public static final String DB_NAME = "couchbasestreet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // And then find it within the content view:
//        ActionButton actionButton = (ActionButton) findViewById(R.id.action_button);
//        actionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Tính click làm chi ku?", Toast.LENGTH_SHORT).show();
//            }
//        });

        setupFab();

        //Get database
        database = createCouch();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Get current location
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);
            //Location location = service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, );
            LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());
            //Animate Camera to mylocation
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation, 16.0f));
        } else {
            // Show rationale and request permission.
        }
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Set Mylocation Enable
            mMap.setMyLocationEnabled(true);

            //Get current location
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);
            //Location location = service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, );
            LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());
            //Animate Camera to mylocation
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation, 17.0f));
        } else {
            // Show rationale and request permission.
        }

        MarkerOptions mp = new MarkerOptions();
        //mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
        mp.position(new LatLng(16.0609723, 108.2161258));
        //mp.icon(BitmapDescriptorFactory.fromResource(R.)));
        mp.title("Đây là khu vực cấm nghe ku");
        mp.snippet("Coi chừng mất mạng đó. Ta đã thử và chết tại đây ^^");
        mMap.addMarker(mp);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Toast.makeText(getApplicationContext(), "Đổi chỗ rồi ku\n" + cameraPosition.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(loc));
//                if(mMap != null){
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//                }
//            }
//        });

//        // Add a marker in HAGL and move the camera
//        LatLng hagl = new LatLng(16.0603087, 108.2143414);
//        mMap.addMarker(new MarkerOptions().position(hagl).title("Marker in HAGL"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hagl));
    }

    @Override
    public void onClick(View v) {
        if (R.id.fab_sheet_item_police == v.getId()) {
            Toast.makeText(getApplicationContext(), "Police", Toast.LENGTH_SHORT).show();
            materialSheetFab.hideSheet();
        } else if (R.id.fab_sheet_item_traffic == v.getId()) {
            Toast.makeText(getApplicationContext(), "Traffic", Toast.LENGTH_SHORT).show();
            materialSheetFab.hideSheet();
        } else if (R.id.fab_sheet_item_speed == v.getId()) {
            Toast.makeText(getApplicationContext(), "Speed", Toast.LENGTH_SHORT).show();
            materialSheetFab.hideSheet();
        } else {
            Toast.makeText(getApplicationContext(), "Other", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets up the Floating action button.
     */
    private void setupFab() {

        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.wallet_bright_foreground_disabled_holo_light);
        int fabColor = getResources().getColor(R.color.colorAccent);


        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);


        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
//                // Save current status bar color
//                statusBarColor = getStatusBarColor();
//                // Set darker status bar color to match the dim overlay
//                setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onHideSheet() {
//                // Restore status bar color
//                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_item_police).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_traffic).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_speed).setOnClickListener(this);
    }

    private Database createCouch() {
        Manager manager = null;
        try {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DB_NAME);
            return database;
        } catch (Exception e) {
            Log.e(DatabaseHelper.TAG, "Error getting database", e);
            return null;
        }
    }


}
