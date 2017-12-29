package cz.optimization.odpadky;


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

import java.util.ArrayList;
import java.util.Arrays;


// TODO oridat info o vybranem typu odpadu - nejlepe rovnou do listy
// TODO naplnit zdroj do SQLite
// TODO po otevreni - upravit lokaci dle aktualni polohy


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        SelectTrashbinDialogFragment.AlertPositiveListener {

    private GoogleMap mMap;
    private int position = 0;

    // Add set of example popelnice objects
    private Popelnice ex1;
    private Popelnice ex2;
    private Popelnice ex3;
    private Popelnice ex4;
    private Popelnice ex5;
    private Popelnice ex6;
    private Popelnice ex7;
    private Popelnice ex8;
    private Popelnice ex9;
    private Popelnice ex10;
    private Popelnice ex11;
    private Popelnice ex12;

    private ArrayList<Popelnice> allAL = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> colourGlassAL = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> whiteGlassAL = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> metalAL = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> plasticAL = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> paperAL = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> cartonAL = new ArrayList<Popelnice>();

    private static final String POSITION_KEY = "position";
    private int previousPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // creating Popelnice object
        ex1 = new Popelnice(50.0527355310001, 14.4178751490001, "Podolská 349/86", "Barevné sklo", 0.95);
        ex2 = new Popelnice(50.1341782590001, 14.4668577360001, "Formánkova 1653/3", "Barevné sklo", 0.80);
        ex3 = new Popelnice(50.034459306, 14.523705712, "Starobylá 881/13", "Čiré sklo", 0.5);
        ex4 = new Popelnice(50.089533063, 14.306763861, "Komárovská 1640", "Čiré sklo", 0.75);
        ex5 = new Popelnice(50.0548082390001, 14.5295966310001, "Miranova 282/11", "Kovy", 0.41);
        ex6 = new Popelnice(50.0926487360001, 14.385085052, "Střešovická 400/3", "Kovy", 0.15);
        ex7 = new Popelnice(50.1266244980001, 14.5775854830001, "K Radonicům 84/16", "Nápojové kartóny", 0.93);
        ex8 = new Popelnice(50.1499694300001, 14.4974263660001, "Na blatech 242/3", "Nápojové kartóny", 1);
        ex9 = new Popelnice(50.0267829960001, 14.6035724650001, "Za pivovarem 1070", "Papír", 0.21);
        ex10 = new Popelnice(50.0914764, 14.432235282, "Petrské náměstí 1206/3", "Papír", 1);
        ex11 = new Popelnice(50.030603252, 14.3099524040001, "K závětinám 727", "Plast", 0.63);
        ex12 = new Popelnice(50.0793594780001, 14.415627511, "Pštrossova 218/27", "Plast", 0.92);

        // creating categories of trashbins as ArrayList
        allAL.addAll(Arrays.asList(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10, ex11, ex12));
        colourGlassAL.addAll(Arrays.asList(ex1, ex2));
        whiteGlassAL.addAll(Arrays.asList(ex3, ex4));
        metalAL.addAll(Arrays.asList(ex5, ex6));
        cartonAL.addAll(Arrays.asList(ex7, ex8));
        paperAL.addAll(Arrays.asList(ex9, ex10));
        plasticAL.addAll(Arrays.asList(ex11, ex12));

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
        float zoomLevel = 10.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ex1.getLat(), ex1.getLong()), zoomLevel));
    }

    @Override
    public void onPositiveClick(int position) {
        this.position = position;

        switch (position) {
            case 0:
                addAllTrashbins();
                break;
            case 1:
                addColourGlassTrashbins();
                break;
            case 2:
                addWhiteGlassTrashbins();
                break;
            case 3:
                addMetalTrashbins();
                break;
            case 4:
                addPlasticTrashbins();
                break;
            case 5:
                addPaperTrashbins();
                break;
            case 6:
                addCartonTrashbins();
                break;
        }
    }

    public void addAllTrashbins() {
        mMap.clear();
        Popelnice location;
        int size = allAL.size();
        for (int i = 0; i < size; i++) {
            location = allAL.get(i);
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(location.getLat(),
                                            location.getLong()))
                            .title(location.getAddress())
                            .snippet("Naplněnost: " + (location.getFullness() * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    public void addColourGlassTrashbins() {
        mMap.clear();
        Popelnice location;
        int size = colourGlassAL.size();
        for (int i = 0; i < size; i++) {
            location = colourGlassAL.get(i);
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(location.getLat(),
                                            location.getLong()))
                            .title(location.getAddress())
                            .snippet("Naplněnost: " + (location.getFullness() * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
    }

    public void addWhiteGlassTrashbins() {
        mMap.clear();
        Popelnice location;
        int size = whiteGlassAL.size();
        for (int i = 0; i < size; i++) {
            location = whiteGlassAL.get(i);
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(location.getLat(),
                                            location.getLong()))
                            .title(location.getAddress())
                            .snippet("Naplněnost: " + (location.getFullness() * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }
    }

    public void addMetalTrashbins() {
        mMap.clear();
        Popelnice location;
        int size = metalAL.size();
        for (int i = 0; i < size; i++) {
            location = metalAL.get(i);
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(location.getLat(),
                                            location.getLong()))
                            .title(location.getAddress())
                            .snippet("Naplněnost: " + (location.getFullness() * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        }
    }

    public void addPlasticTrashbins() {
        mMap.clear();
        Popelnice location;
        int size = plasticAL.size();
        for (int i = 0; i < size; i++) {
            location = plasticAL.get(i);
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(location.getLat(),
                                            location.getLong()))
                            .title(location.getAddress())
                            .snippet("Naplněnost: " + (location.getFullness() * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
    }

    public void addPaperTrashbins() {
        mMap.clear();
        Popelnice location;
        int size = paperAL.size();
        for (int i = 0; i < size; i++) {
            location = paperAL.get(i);
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(location.getLat(),
                                            location.getLong()))
                            .title(location.getAddress())
                            .snippet("Naplněnost: " + (location.getFullness() * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    public void addCartonTrashbins() {
        mMap.clear();
        Popelnice location;
        int size = cartonAL.size();
        for (int i = 0; i < size; i++) {
            location = cartonAL.get(i);
            Marker m = mMap
                    .addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(location.getLat(),
                                            location.getLong()))
                            .title(location.getAddress())
                            .snippet("Naplněnost: " + (location.getFullness() * 100) + " %")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }
    }
}
