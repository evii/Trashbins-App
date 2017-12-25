package cz.optimization.odpadky;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static java.security.AccessController.getContext;
import static java.util.Collections.addAll;


// TODO preskocit jeden krok okno s radkem Druh odpadu - rovnou na preferences
// TODO oridat info o vybranem typu odpadu - nejlepe rovnou do listy
// TODO naplnit zdroj do SQLite
// TODO po otevreni - upravit lokaci dle aktualni polohy


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MainActivity";
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

    private ArrayList<Popelnice> all = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> coulourGlass = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> whiteGlass = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> metal = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> plastic = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> paper = new ArrayList<Popelnice>();
    private ArrayList<Popelnice> carton = new ArrayList<Popelnice>();

    private LatLng ex1LL;
    private LatLng ex2LL;
    private LatLng ex3LL;
    private LatLng ex4LL;
    private LatLng ex5LL;
    private LatLng ex6LL;
    private LatLng ex7LL;
    private LatLng ex8LL;
    private LatLng ex9LL;
    private LatLng ex10LL;
    private LatLng ex11LL;
    private LatLng ex12LL;

    private DialogInterface.OnClickListener selectedTrashbin = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    mMap.clear();
                    mMap.addMarker(new
                            MarkerOptions().position(ex1LL).title(ex1.getAddress()).snippet("Naplněnost: " + (ex1.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex2LL).title(ex2.getAddress()).snippet("Naplněnost: " + (ex2.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex3LL).title(ex3.getAddress()).snippet("Naplněnost: " + (ex3.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex4LL).title(ex4.getAddress()).snippet("Naplněnost: " + (ex4.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex5LL).title(ex5.getAddress()).snippet("Naplněnost: " + (ex5.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex6LL).title(ex6.getAddress()).snippet("Naplněnost: " + (ex6.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex7LL).title(ex7.getAddress()).snippet("Naplněnost: " + (ex7.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex8LL).title(ex8.getAddress()).snippet("Naplněnost: " + (ex8.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex9LL).title(ex9.getAddress()).snippet("Naplněnost: " + (ex9.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex10LL).title(ex10.getAddress()).snippet("Naplněnost: " + (ex10.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex11LL).title(ex11.getAddress()).snippet("Naplněnost: " + (ex11.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex12LL).title(ex12.getAddress()).snippet("Naplněnost: " + (ex12.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    Log.v(TAG, "case all");
                    break;
                case 1:
                    mMap.clear();
                    mMap.addMarker(new
                            MarkerOptions().position(ex1LL).title(ex1.getAddress()).snippet("Naplněnost: " + (ex1.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex2LL).title(ex2.getAddress()).snippet("Naplněnost: " + (ex2.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    Log.v(TAG, "case colourglass");
                    break;
                case 2:
                    mMap.clear();
                    mMap.addMarker(new
                            MarkerOptions().position(ex3LL).title(ex3.getAddress()).snippet("Naplněnost: " + (ex3.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex4LL).title(ex4.getAddress()).snippet("Naplněnost: " + (ex4.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    Log.v(TAG, "case whiteglass");
                    break;
                case 3:
                    mMap.clear();
                    mMap.addMarker(new
                            MarkerOptions().position(ex5LL).title(ex5.getAddress()).snippet("Naplněnost: " + (ex5.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex6LL).title(ex6.getAddress()).snippet("Naplněnost: " + (ex6.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    Log.v(TAG, "case metal");
                    break;
                case 4:
                    mMap.clear();
                    mMap.addMarker(new
                            MarkerOptions().position(ex11LL).title(ex11.getAddress()).snippet("Naplněnost: " + (ex11.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex12LL).title(ex12.getAddress()).snippet("Naplněnost: " + (ex12.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    Log.v(TAG, "case plastic");
                    break;
                case 5:
                    mMap.clear();
                    mMap.addMarker(new
                            MarkerOptions().position(ex9LL).title(ex9.getAddress()).snippet("Naplněnost: " + (ex9.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex10LL).title(ex10.getAddress()).snippet("Naplněnost: " + (ex10.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    Log.v(TAG, "case paper");
                    break;
                case 6:
                    mMap.clear();
                    mMap.addMarker(new
                            MarkerOptions().position(ex7LL).title(ex7.getAddress()).snippet("Naplněnost: " + (ex7.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.addMarker(new
                            MarkerOptions().position(ex8LL).title(ex8.getAddress()).snippet("Naplněnost: " + (ex8.getFullness() * 100) + " %").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    Log.v(TAG, "case carton");
                    break;

            }
        }
    };


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
        all.addAll(Arrays.asList(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10, ex11, ex12));
        coulourGlass.addAll(Arrays.asList(ex1, ex2));
        whiteGlass.addAll(Arrays.asList(ex3, ex4));
        metal.addAll(Arrays.asList(ex5, ex6));
        carton.addAll(Arrays.asList(ex7, ex8));
        paper.addAll(Arrays.asList(ex9, ex10));
        plastic.addAll(Arrays.asList(ex11, ex12));

        //getting position of each trashbin
        ex1LL = new LatLng(ex1.getLat(), ex1.getLong());
        ex2LL = new LatLng(ex2.getLat(), ex2.getLong());
        ex3LL = new LatLng(ex3.getLat(), ex3.getLong());
        ex4LL = new LatLng(ex4.getLat(), ex4.getLong());
        ex5LL = new LatLng(ex5.getLat(), ex5.getLong());
        ex6LL = new LatLng(ex6.getLat(), ex6.getLong());
        ex7LL = new LatLng(ex7.getLat(), ex7.getLong());
        ex8LL = new LatLng(ex8.getLat(), ex8.getLong());
        ex9LL = new LatLng(ex9.getLat(), ex9.getLong());
        ex10LL = new LatLng(ex10.getLat(), ex10.getLong());
        ex11LL = new LatLng(ex11.getLat(), ex11.getLong());
        ex12LL = new LatLng(ex12.getLat(), ex12.getLong());


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
            dialog.setCallBack(selectedTrashbin);
            dialog.show(fm, "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // LatLng TutorialsPoint = new LatLng(50.089468, 14.472460);
        //mMap.addMarker(new
        //      MarkerOptions().position(TutorialsPoint).title("Ambro"));

        float zoomLevel = 10.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ex1LL, zoomLevel));
    }

}
