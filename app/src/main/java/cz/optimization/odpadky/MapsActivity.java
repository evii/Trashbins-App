package cz.optimization.odpadky;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.concurrent.ExecutionException;

import cz.optimization.odpadky.data.TrashbinContract;
import cz.optimization.odpadky.data.TrashbinDbHelper;

import static java.lang.Math.round;

// TODO po otevreni - upravit lokaci dle aktualni polohy

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        SelectTrashbinDialogFragment.AlertPositiveListener {

    private GoogleMap mMap;
    private int position = 0;

    TrashbinDbHelper dbHelper;
    Cursor cursor;

    private static final String POSITION_KEY = "position";
    private int previousPosition;

    private ClusterManager<TrashbinClusterItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(POSITION_KEY)) {
                previousPosition = savedInstanceState.getInt(POSITION_KEY);
            }
        } else {
            previousPosition = position;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_KEY, position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filtr) {

            FragmentManager fm = getSupportFragmentManager();
            SelectTrashbinDialogFragment dialog = new SelectTrashbinDialogFragment();

            // Creating a bundle object to store the selected item's index
            Bundle b = new Bundle();

            // Storing the selected item's index in the bundle object
            b.putInt("position", position);

            // Setting the bundle object to the dialog fragment object
            dialog.setArguments(b);

            dialog.show(fm, "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        onPositiveClick(previousPosition);
        float zoomLevel = 15.5f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.0889853530001, 14.4723441130001), zoomLevel));
    }

    @Override
    public void onPositiveClick(int position) {
        this.position = position;
        dbHelper = new TrashbinDbHelper(this);
        dbHelper.createOpenDb();

        switch (position) {
            case 0:
                setTitle("Optimization - All trashbins");
                try {
                    cursor = dbHelper.selectAllPoints();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                setTitle("Optimization - Colour glass trashbins");
                try {
                    cursor = dbHelper.selectOneTypePoints(new String[]{"BS"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                setTitle("Optimization - White glass trashbins");
                try {
                    cursor = dbHelper.selectOneTypePoints(new String[]{"CS"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                setTitle("Optimization - Metal trashbins");
                try {
                    cursor = dbHelper.selectOneTypePoints(new String[]{"K"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                setTitle("Optimization - Plastic trashbins");
                try {
                    cursor = dbHelper.selectOneTypePoints(new String[]{"PL"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                setTitle("Optimization - Paper trashbins");
                try {
                    cursor = dbHelper.selectOneTypePoints(new String[]{"PA"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                setTitle("Optimization - Carton UHT trashbins");
                try {
                    cursor = dbHelper.selectOneTypePoints(new String[]{"NK"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
        addPoints(cursor);
    }

    private void addPoints(Cursor cursor) {

        int locationCount = 0;
        String address = "";
        double lat = 0;
        double lng = 0;
        double progress = 0;
     //   String trashType = "";
    //    Float colour = BitmapDescriptorFactory.HUE_ROSE;
        String snippet = "";


        mMap.clear();
        // Number of locations available in the SQLite database table
        locationCount = cursor.getCount();

        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);
        // Move the current record pointer to the first row of the table
        cursor.moveToFirst();

        for (int i = 0; i < locationCount; i++) {

            lat = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_LAT));
            lng = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_LONG));

            address = cursor.getString(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_ADDRESS));
            progress = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_PROGRESS));
            double progressRounded = (double) Math.round(progress * 100) / 100;
            snippet = "Naplněnost: " + (progressRounded * 100) + " %";

            // Getting colour of the point
            /** trashType = cursor.getString(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_TRASHTYPE_INDEX));

            switch (trashType) {
                case "BS":
                    colour = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case "CS":
                    colour = BitmapDescriptorFactory.HUE_AZURE;
                    break;
                case "K":
                    colour = BitmapDescriptorFactory.HUE_MAGENTA;
                    break;
                case "NK":
                    colour = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case "PA":
                    colour = BitmapDescriptorFactory.HUE_BLUE;
                    break;
                case "PL":
                    colour = BitmapDescriptorFactory.HUE_YELLOW;
                    break;
            }
*/
            // Drawing the marker in the Google Maps
            mClusterManager.addItem(new TrashbinClusterItem(lat,lng,address,snippet));

            // Traverse the pointer to the next row
            cursor.moveToNext();
        }
        mClusterManager.cluster();
    }
}
