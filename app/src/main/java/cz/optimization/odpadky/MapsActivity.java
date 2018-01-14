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

import java.util.concurrent.ExecutionException;

import cz.optimization.odpadky.data.TrashbinContract;
import cz.optimization.odpadky.data.TrashbinDbHelper;


// TODO pridat info o vybranem typu odpadu - nejlepe rovnou do listy
// TODO po otevreni - upravit lokaci dle aktualni polohy


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        SelectTrashbinDialogFragment.AlertPositiveListener {

    private GoogleMap mMap;
    private int position = 0;

    TrashbinDbHelper dbHelper;
    Cursor cursor;

    private static final String POSITION_KEY = "position";
    private int previousPosition;

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
                try {
                    cursor = dbHelper.selectAllPoints();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    cursor = dbHelper.selectOneTypePoints(new String [] {"BS"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    cursor = dbHelper.selectOneTypePoints(new String [] {"CS"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    cursor = dbHelper.selectOneTypePoints(new String [] {"K"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    cursor = dbHelper.selectOneTypePoints(new String [] {"PL"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    cursor = dbHelper.selectOneTypePoints(new String [] {"PA"});
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    cursor = dbHelper.selectOneTypePoints(new String [] {"NK"});
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
        String trashType = "";
        Float colour = BitmapDescriptorFactory.HUE_ROSE;

        mMap.clear();
        // Number of locations available in the SQLite database table
        locationCount = cursor.getCount();

        // Move the current record pointer to the first row of the table
        cursor.moveToFirst();

        for (int i = 0; i < locationCount; i++) {

            lat = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_LAT));
            lng = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_LONG));
            LatLng location = new LatLng(lat, lng);

            address = cursor.getString(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_ADDRESS));

            progress = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_PROGRESS));

            trashType = cursor.getString(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_TRASHTYPE_INDEX));

            // Getting colour of the point
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

            // Drawing the marker in the Google Maps
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(location)
                            .title(address)
                            .snippet("Naplněnost: " + (progress * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(colour)));

            // Traverse the pointer to the next row
            cursor.moveToNext();
        }
    }
}
