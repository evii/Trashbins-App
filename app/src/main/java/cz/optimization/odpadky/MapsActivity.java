package cz.optimization.odpadky;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.retrofit_data.APIClient;
import cz.optimization.odpadky.retrofit_data.GetDataService;
import cz.optimization.odpadky.objects.Place;
import cz.optimization.odpadky.del.TrashbinDbHelper;
import cz.optimization.odpadky.ui.DetailActivity;
import cz.optimization.odpadky.ui.TrashbinAppWidgetProvider;
import cz.optimization.odpadky.ui.clusters.CustomClusterRenderer;
import cz.optimization.odpadky.ui.clusters.TrashbinClusterItem;
import cz.optimization.odpadky.ui.info_window.CustomInfoWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// TODO Layout Detail Activity
// TODO Zkontroluj update dialogu watched places
// TODO Pridej funkci show pro polozku dialogu - watched list
// TODO Pridej Delete function listu watched places
// TODO zmena barvy markeru dle typu odpadu
// TODO progress bar pro otvirani lokaci
// TODO funkce widgetu - aby otevrel pouze vybranou kategorii kontejneru
// TODO saved instance po zmene orientace - vsude
// TODO sipka zpet v Detail ASctivity - funkce
// TODO otevreni dle aktualni lokace - vysvetleni pro reviewera
// TODO transition between activities



public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        SelectTrashbinTypeDialogFragment.AlertPositiveListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private int position = 0;

    private static final String POSITION_KEY = "position";
    private int previousPosition;

    private ClusterManager<TrashbinClusterItem> mClusterManager;
    private List<Place> mListPlaces;
    private CustomClusterRenderer renderer;
    private TrashbinClusterItem trashbinClusterItem;

    public static final String PREFS_NAME = "Containers_object";
    public static final String PREFS_KEY = "Containers_key";
    private SharedPreferences sharedPreferences;

    private static final String TAG = "MapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

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
        inflater.inflate(R.menu.maps_activity_action_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filtr) {

            FragmentManager fm = getSupportFragmentManager();
            SelectTrashbinTypeDialogFragment dialog = new SelectTrashbinTypeDialogFragment();

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

        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(this);

        mClusterManager = new ClusterManager<>(getBaseContext(), mMap);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        renderer = new CustomClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);
        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new CustomInfoWindow(MapsActivity.this));


        float zoomLevel = 15.5f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.0889853530001, 14.4723441130001), zoomLevel));


        String widgetClicked = "";
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            widgetClicked= null;
            onPositiveClick(previousPosition);
        } else {
            widgetClicked= extras.getString("widget_clicked");

            if (widgetClicked.equals(TrashbinAppWidgetProvider.METAL_BUTTON)){
                Log.v("Widgetwigd",widgetClicked+" "+ TrashbinAppWidgetProvider.METAL_BUTTON );
                mMap.clear();
                setTitle(R.string.metal_title);
                position = 3;

                fetchContainersType(getResources().getString(R.string.metal));

               // mClusterManager.cluster();
            }
        }
    }

    @Override
    public void onPositiveClick(int position) {
        this.position = position;



        switch (position) {
            case 0:
                setTitle(R.string.all_title);
                mMap.clear();
                fetchPlaces();


                break;
            case 1:
                setTitle(R.string.glass_title);
                mMap.clear();
                fetchContainersType(getResources().getString(R.string.glass));

                break;
            case 2:
                setTitle(R.string.clear_glass_title);
                mMap.clear();
                fetchContainersType(getResources().getString(R.string.clear_glass));

                break;
            case 3:
                setTitle(R.string.metal_title);
                mMap.clear();
                fetchContainersType(getResources().getString(R.string.metal));

                break;
            case 4:
                setTitle(R.string.plastic_title);
                mMap.clear();
                fetchContainersType(getResources().getString(R.string.plastic));

                break;
            case 5:
                setTitle(R.string.paper_title);
                mMap.clear();
                fetchContainersType(getResources().getString(R.string.paper));

                break;
            case 6:
                setTitle(R.string.carton_title);
                mMap.clear();
                fetchContainersType(getResources().getString(R.string.carton));

                break;

            case 7:
                setTitle(R.string.electrical_title);
                mMap.clear();
                fetchContainersType(getResources().getString(R.string.electrical));

                break;

        }
     //   mClusterManager.cluster();

    }

    // opening DetailActivity after clicking on Info Window
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, DetailActivity.class);
        String title = marker.getTitle();
        String placeId = marker.getSnippet();
        String containersList = "";
        if (sharedPreferences.contains(PREFS_KEY)) {
            containersList = sharedPreferences.getString(PREFS_KEY, "");
        }
        else {
            Log.d(TAG, "ContainersList not contained in Shared preferences");
        }


        intent.putExtra("placeId", placeId);
        intent.putExtra("title", title);
        intent.putExtra("containersList", containersList);
        startActivity(intent);
    }

    // helper method to get the places from the API - using retrofit + seting onclicklisteners on markers
    public void fetchPlaces() {

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        Call<List<Place>> call = service.getAllPlaces();
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {

                mListPlaces = response.body();

                mClusterManager.addItems(getPlaceLocation(mListPlaces));
                mClusterManager.cluster();


                // onclick listener for cluster
                mClusterManager.setOnClusterClickListener(
                        new ClusterManager.OnClusterClickListener<TrashbinClusterItem>() {
                            @Override
                            public boolean onClusterClick(Cluster<TrashbinClusterItem> cluster) {

                                Toast.makeText(MapsActivity.this, R.string.Cluster_click, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                // onclicklistener for marker
                mClusterManager.setOnClusterItemClickListener(
                        new ClusterManager.OnClusterItemClickListener<TrashbinClusterItem>() {
                            @Override
                            public boolean onClusterItemClick(TrashbinClusterItem clusterItem) {

                                trashbinClusterItem =clusterItem;


                                //get marker from clusterItem

                                String placeId = clusterItem.getSnippet();
                                Log.v("JsonParseMAPS", placeId);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("placeid", placeId);
                                editor.commit();


                                //get Containers for given placeId
                                fetchContainersAtPlace(placeId);


                                return false;
                            }
                        });

            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {

                Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

    // helper method to transfer the List of places into the cluster items
    private List<TrashbinClusterItem> getPlaceLocation(List<Place> places) {
        List<TrashbinClusterItem> ListItems = new ArrayList<>();

        int locationCount = places.size();
        String address = "";
        double lat = 0;
        double lng = 0;
        String placeId = "";

        for (int i = 0; i < locationCount; i++) {
            Place place = places.get(i);

            lat = place.getLatitude();
            lng = place.getLongitude();
            address = place.getTitle();
            placeId = place.getPlaceId();

            trashbinClusterItem = new TrashbinClusterItem(lat, lng, address, placeId);
            // Adding the marker to the List
            ListItems.add(trashbinClusterItem);
        }
        return ListItems;
    }

    // helper method to get details of containers in a place
    private void fetchContainersAtPlace(String placeId) {

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        Call<Container.ContainersResult> call = service.getContainersList(placeId);
        call.enqueue(new Callback<Container.ContainersResult>() {
            @Override
            public void onResponse(Call<Container.ContainersResult> call, Response<Container.ContainersResult> response) {
                int urlResp = response.code();
                Log.v ("URLCODE", String.valueOf(urlResp));

                List<Container> containers = response.body().getResults();

                Gson gson = new Gson();
                String containersString = gson.toJson(containers);

                // Puting data about Containers at given place to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREFS_KEY, containersString);
                editor.commit();

                // pass the Containers list to info window
                Marker marker = renderer.getMarker(trashbinClusterItem);
                marker.setTag(containersString);
                marker.showInfoWindow();


            }

            @Override
            public void onFailure(Call<Container.ContainersResult> call, Throwable t) {

                Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

    // helper method to display containers of selected type
    public void fetchContainersType(final String trashTypeSelected) {
        GetDataService service = APIClient.getClient().create(GetDataService.class);
if( mListPlaces == null) {

    Call<List<Place>> call1 = service.getAllPlaces();
    call1.enqueue(new Callback<List<Place>>() {
        @Override
        public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
            mListPlaces = response.body();
        }

        @Override
        public void onFailure(Call<List<Place>> call, Throwable t) {

            Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
        }
    });
}

 Call<List<Container>> call2 = service.getContainersTypes();
        call2.enqueue(new Callback<List<Container>>() {
            @Override
            public void onResponse(Call<List<Container>> call, Response<List<Container>> response) {

                List<Container> allContainersList = response.body();
                int urlResp = response.code();
                Log.v("containRetro allCont: ", String.valueOf(allContainersList.size()) + " " + String.valueOf(urlResp));

                //covert list of places with coordinates into array map
                ArrayMap<String, Place> allPlacesMap = new ArrayMap<String, Place>();
                for (Place place : mListPlaces) {
                    allPlacesMap.put(place.getPlaceId(), place);
                }
                Log.v("containRetro mListP: ", String.valueOf(mListPlaces.size()));


                // create new containers list with coordinates
                List<Container> containerCoordinatesList = new ArrayList<Container>();
                String trashType = "";
                for (Container container : allContainersList) {
                    trashType = container.getTrashType();

                    if (trashType.equals(trashTypeSelected)) {

                        String placeId = container.getPlaceId();
                        Place selectedPlace = allPlacesMap.get(placeId);
                        double lat = selectedPlace.getLatitude();
                        double lng = selectedPlace.getLongitude();

                        int binId = container.getBinId();
                        String underground = container.getUnderground();
                        String cleaning = container.getCleaning();
                        int progress = container.getProgress();

                        Container containerCoordinates = new Container(placeId, trashType, binId, underground, cleaning, progress, lat, lng);
                        containerCoordinatesList.add(containerCoordinates);
                    }
                }

                mClusterManager.addItems(getContainersLocation(containerCoordinatesList));
                Log.v("containRetro contCoord", String.valueOf(containerCoordinatesList.size()));
                mClusterManager.cluster();
            }

            @Override
            public void onFailure(Call<List<Container>> call, Throwable t) {

                Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

    // helper method to get clusteritems for container of selected type
    private List<TrashbinClusterItem> getContainersLocation(List<Container> containers) {
        List<TrashbinClusterItem> ListItems = new ArrayList<>();

        int locationCount = containers.size();
        String trashType = "";
        double lat = 0;
        double lng = 0;
        String placeId = "";

        for (int i = 0; i < locationCount; i++) {
            Container container = containers.get(i);

            lat = container.getLatitude();
            lng = container.getLongitude();
            trashType = container.getTrashType();
            placeId = String.valueOf(container.getPlaceId());

            // Adding the marker to the List
            ListItems.add(new TrashbinClusterItem(lat, lng, trashType, placeId));
        }
        return ListItems;
    }

}



