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

import java.util.List;
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
    private List<TrashbinClusterItem> mListLocations;

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

        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        switch (position) {
            case 0:
                setTitle("Optimization - All trashbins");
                mMap.clear();
                try {
                    mListLocations = dbHelper.selectAllPoints();
                    mClusterManager.addItems(mListLocations);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                setTitle("Optimization - Colour glass trashbins");
                mMap.clear();
                try {
                    mListLocations = dbHelper.selectOneTypePoints(new String[]{"BS"});
                    mClusterManager.addItems(mListLocations);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                setTitle("Optimization - White glass trashbins");
                mMap.clear();
                try {
                    mListLocations = dbHelper.selectOneTypePoints(new String[]{"CS"});
                    mClusterManager.addItems(mListLocations);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                setTitle("Optimization - Metal trashbins");
                mMap.clear();
                try {
                    mListLocations = dbHelper.selectOneTypePoints(new String[]{"K"});
                    mClusterManager.addItems(mListLocations);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                setTitle("Optimization - Plastic trashbins");
                mMap.clear();
                try {
                    mListLocations = dbHelper.selectOneTypePoints(new String[]{"PL"});
                    mClusterManager.addItems(mListLocations);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                setTitle("Optimization - Paper trashbins");
                mMap.clear();
                try {
                    mListLocations = dbHelper.selectOneTypePoints(new String[]{"PA"});
                    mClusterManager.addItems(mListLocations);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                setTitle("Optimization - Carton UHT trashbins");
                mMap.clear();
                try {
                    mListLocations = dbHelper.selectOneTypePoints(new String[]{"NK"});
                    mClusterManager.addItems(mListLocations);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
        mClusterManager.cluster();

    }
    }

