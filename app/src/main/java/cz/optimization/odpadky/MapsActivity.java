package cz.optimization.odpadky;

import android.app.ActivityOptions;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.retrofit_data.APIClient;
import cz.optimization.odpadky.retrofit_data.FetchContainersAtPlaceViewModel;
import cz.optimization.odpadky.retrofit_data.FetchContainersAtPlaceViewModelFactory;
import cz.optimization.odpadky.retrofit_data.FetchContainersTypeViewModel;
import cz.optimization.odpadky.retrofit_data.FetchContainersTypeViewModelFactory;
import cz.optimization.odpadky.retrofit_data.GetDataService;
import cz.optimization.odpadky.objects.Place;
import cz.optimization.odpadky.ui.DetailActivity;
import cz.optimization.odpadky.ui.TrashbinAppWidgetProvider;
import cz.optimization.odpadky.ui.clusters.CustomClusterRenderer;
import cz.optimization.odpadky.ui.clusters.TrashbinClusterItem;
import cz.optimization.odpadky.ui.info_window.CustomInfoWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        SelectTrashbinTypeDialogFragment.AlertPositiveListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    public static int position = 0;
    private double mHomeLatitude;
    private double mHomeLongitude;
    private float mCameraZoom;

    private static final String POSITION_KEY = "position";
    private static final String LAT_KEY = "latitude";
    private static final String LNG_KEY = "longitude";
    private static final String ZOOM_KEY = "zoom";
    private static final String LISTPLACES_KEY = "listPlaces";
    private static final String LISTCONTAINERS_KEY = "listContainers";
    private static final String INFOPLACEID_KEY = "infowindowplaceid";
    private static final String CLUSTERITEM_KEY = "clusteritem";
    private static final String INFOWINDOW_KEY = "infowindow";

    private int previousPosition;

    private ClusterManager<TrashbinClusterItem> mClusterManager;
    private List<Place> mListPlaces;
    private String mListPlacesString;
    private CustomClusterRenderer renderer;
    private TrashbinClusterItem trashbinClusterItem;
    private String mAllContainersListString;

    public static final String PREFS_NAME = "Containers_object";
    public static final String PREFS_KEY = "Containers_key";
    private SharedPreferences sharedPreferences;

    private static final String TAG = "MapsActivity";

    private ProgressBar mProgressBar;
    private Marker mMarker;
    private Map<TrashbinClusterItem, Marker> mClusterMarkerMap;

    private Boolean isInfoDisplayed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mClusterMarkerMap = new HashMap<>();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //intial position of map
        mHomeLatitude = 50.0889853530001;
        mHomeLongitude = 14.4723441130001;
        mCameraZoom = 15.5f;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*if(savedInstanceState == null){
            mapFragment.setRetainInstance(true);   // maintains the map fragment on orientation changes
        }*/


        // restoring position and zoom on the map
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(POSITION_KEY)) {
                previousPosition = savedInstanceState.getInt(POSITION_KEY);
            } else {
                previousPosition = 0;
            }
            mHomeLatitude = savedInstanceState.getDouble(LAT_KEY);
            mHomeLongitude = savedInstanceState.getDouble(LNG_KEY);
            mCameraZoom = savedInstanceState.getFloat(ZOOM_KEY);

        } else {
            previousPosition = position;
        }

        mProgressBar = findViewById(R.id.progress_bar);


    }

    //saving position and zoom on the map
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saving selection from the dilog of types
        outState.putInt(POSITION_KEY, position);

        // saving position of map
        LatLng mapPosition = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
        float zoom = mMap.getCameraPosition().zoom;
        double lat = mapPosition.latitude;
        double lng = mapPosition.longitude;
        outState.putFloat(ZOOM_KEY, zoom);
        outState.putDouble(LAT_KEY, lat);
        outState.putDouble(LNG_KEY, lng);

        //saving already fetched data from API
        outState.putString(LISTPLACES_KEY, mListPlacesString);
        outState.putString(LISTCONTAINERS_KEY, mAllContainersListString);

        // save open info window in cae of rotation
        if (mMarker != null){
            Log.v ("infowzobr", "marker is not null");
            isInfoDisplayed = mMarker.isInfoWindowShown();
            if (isInfoDisplayed) {
                String placeIdInfo = mMarker.getSnippet();
                //markerLat = mMarker.getPosition().latitude;
                //markerLng = mMarker.getPosition().longitude;
                outState.putString(INFOPLACEID_KEY, placeIdInfo);
              //  outState.putDouble(MARKERLAT_KEY, markerLat);
             //   outState.putDouble(MARKERLNG_KEY, markerLng);
                outState.putParcelable(CLUSTERITEM_KEY, trashbinClusterItem);
                mClusterMarkerMap.put(trashbinClusterItem, mMarker);
                String markerId = mMarker.getId();
                outState.putString("markerId", markerId);

            }
            Log.v("infowzobrsav", String.valueOf(isInfoDisplayed));
            outState.putBoolean(INFOWINDOW_KEY, isInfoDisplayed);
        }


    }

    // restoring position and zoom on the map
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHomeLatitude = savedInstanceState.getDouble(LAT_KEY);
        mHomeLongitude = savedInstanceState.getDouble(LNG_KEY);
        mCameraZoom = savedInstanceState.getFloat(ZOOM_KEY);
        mListPlacesString = savedInstanceState.getString(LISTPLACES_KEY);
        mAllContainersListString = savedInstanceState.getString(LISTCONTAINERS_KEY);


        // restore open info window after rotation
        isInfoDisplayed = savedInstanceState.getBoolean(INFOWINDOW_KEY);
        if(isInfoDisplayed){
            String placeId = savedInstanceState.getString(INFOPLACEID_KEY);
          //  markerLat = savedInstanceState.getDouble(MARKERLAT_KEY);
            //markerLng = savedInstanceState.getDouble(MARKERLNG_KEY);
            trashbinClusterItem = savedInstanceState.getParcelable(CLUSTERITEM_KEY);

            Log.v("infowzobrres", placeId + "");

            FetchContainersAtPlaceViewModelFactory factory = new FetchContainersAtPlaceViewModelFactory(getApplication(), placeId);
            FetchContainersAtPlaceViewModel viewModel = ViewModelProviders.of(MapsActivity.this, factory)
                    .get(FetchContainersAtPlaceViewModel.class);

            viewModel.FetchContainersAtPlace(placeId).observe(MapsActivity.this, new Observer<List<Container>>() {

                @Override
                public void onChanged(List<Container> containers) {

                    Gson gson = new Gson();
                    String containersString = gson.toJson(containers);

                    // Puting data about Containers at given place to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREFS_KEY, containersString);
                    editor.commit();

                    renderer = new CustomClusterRenderer(MapsActivity.this, mMap, mClusterManager);
                    mMarker = renderer.getMarker(trashbinClusterItem);
                    Log.v("infowzobr", trashbinClusterItem.toString()+" clusteritem " );
                    Log.v("infowzobr", renderer.toString()+" renderer" );
                  //  mMarker.showInfoWindow();


                }
            });




        }

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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mHomeLatitude, mHomeLongitude), mCameraZoom));


        // opening first screen
        onPositiveClick(previousPosition);

        //WIGDET - display correct containers after clicking from the widget
        String widgetClicked = "";

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            widgetClicked = null;
            onPositiveClick(previousPosition);
        } else if (extras.getString(TrashbinAppWidgetProvider.WIDGET_CLICKED_KEY) != null) {
            widgetClicked = extras.getString(TrashbinAppWidgetProvider.WIDGET_CLICKED_KEY);

            switch (widgetClicked) {
                case TrashbinAppWidgetProvider.ALL_BUTTON:
                    mMap.clear();
                    setTitle(R.string.all_title);
                    position = 0;
                    fetchPlaces();
                    break;

                case TrashbinAppWidgetProvider.GLASS_BUTTON:
                    mMap.clear();
                    setTitle(R.string.glass_title);
                    position = 1;
                    fetchContainersType(getResources().getString(R.string.glass));
                    break;

                case TrashbinAppWidgetProvider.CLEAR_GLASS_BUTTON:
                    mMap.clear();
                    setTitle(R.string.clear_glass_title);
                    position = 2;
                    fetchContainersType(getResources().getString(R.string.clear_glass));
                    break;

                case TrashbinAppWidgetProvider.METAL_BUTTON:
                    mMap.clear();
                    setTitle(R.string.metal_title);
                    position = 3;
                    fetchContainersType(getResources().getString(R.string.metal));
                    break;

                case TrashbinAppWidgetProvider.PLASTIC_BUTTON:
                    mMap.clear();
                    setTitle(R.string.plastic_title);
                    position = 4;
                    fetchContainersType(getResources().getString(R.string.plastic));
                    break;

                case TrashbinAppWidgetProvider.PAPER_BUTTON:
                    mMap.clear();
                    setTitle(R.string.paper_title);
                    position = 5;
                    fetchContainersType(getResources().getString(R.string.paper));
                    break;

                case TrashbinAppWidgetProvider.CARTON_BUTTON:
                    mMap.clear();
                    setTitle(R.string.carton_title);
                    position = 6;
                    fetchContainersType(getResources().getString(R.string.carton));
                    break;

                case TrashbinAppWidgetProvider.ELECTRICAL_BUTTON:
                    mMap.clear();
                    setTitle(R.string.electrical_title);
                    position = 7;
                    fetchContainersType(getResources().getString(R.string.electrical));
                    break;
            }
        }
    }

    //Filter in action bar
    @Override
    public void onPositiveClick(int position) {
        this.position = position;

        switch (position) {
            case 0:
                setTitle(R.string.all_title);
                fetchPlaces();
                break;

            case 1:
                setTitle(R.string.glass_title);
                fetchContainersType(getResources().getString(R.string.glass));
                break;

            case 2:
                setTitle(R.string.clear_glass_title);
                fetchContainersType(getResources().getString(R.string.clear_glass));
                break;

            case 3:
                setTitle(R.string.metal_title);
                fetchContainersType(getResources().getString(R.string.metal));
                break;

            case 4:
                setTitle(R.string.plastic_title);
                fetchContainersType(getResources().getString(R.string.plastic));
                break;

            case 5:
                setTitle(R.string.paper_title);
                fetchContainersType(getResources().getString(R.string.paper));
                break;

            case 6:
                setTitle(R.string.carton_title);
                fetchContainersType(getResources().getString(R.string.carton));
                break;

            case 7:
                setTitle(R.string.electrical_title);
                fetchContainersType(getResources().getString(R.string.electrical));
                break;

        }
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
        } else {
            Log.d(TAG, "ContainersList not contained in Shared preferences");
        }

        intent.putExtra("placeId", placeId);
        intent.putExtra("title", title);
        intent.putExtra("containersList", containersList);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    // helper method to get the places from the API - using retrofit + setting onclicklisteners on markers
    public void fetchPlaces() {

        // call to API for places made for the first time
        if (mListPlacesString == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(true);
            mMap.clear();

            GetDataService service = APIClient.getClient().create(GetDataService.class);
            Call<List<Place>> call = service.getAllPlaces();
            call.enqueue(new Callback<List<Place>>() {
                @Override
                public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {

                    mListPlaces = response.body();
                    Gson gson = new Gson();
                    mListPlacesString = gson.toJson(mListPlaces);
                    fetchPlacesHelper(mListPlaces);

                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<Place>> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                }
            });
            // the API call was already done and data about places fetched and saved.
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Place>>() {
            }.getType();
            mListPlaces = gson.fromJson(mListPlacesString, type);
            fetchPlacesHelper(mListPlaces);
        }
    }

    // helper method to avoid doubling the code - to fechtPlaces() method
    private void fetchPlacesHelper(List<Place> listPlaces) {

        mClusterManager.clearItems();
        mClusterManager.addItems(getPlaceLocation(listPlaces));
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

                        trashbinClusterItem = clusterItem;

                        //get marker from clusterItem
                        final String placeId = clusterItem.getSnippet();

                        // helper method to get details of containers in one place
                        FetchContainersAtPlaceViewModelFactory factory = new FetchContainersAtPlaceViewModelFactory(getApplication(), placeId);
                        FetchContainersAtPlaceViewModel viewModel = ViewModelProviders.of(MapsActivity.this, factory)
                                .get(FetchContainersAtPlaceViewModel.class);

                        viewModel.FetchContainersAtPlace(placeId).observe(MapsActivity.this, new Observer<List<Container>>() {

                            @Override
                            public void onChanged(List<Container> containers) {

                                Gson gson = new Gson();
                                String containersString = gson.toJson(containers);

                                // Puting data about Containers at given place to SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(PREFS_KEY, containersString);
                                editor.commit();

                                mMarker = renderer.getMarker(trashbinClusterItem);
                                mMarker.showInfoWindow();

                            }
                        });

                        return false;
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

    // helper method to display containers of selected type
    public void fetchContainersType(final String trashTypeSelected) {

        /*if (mAllContainersListString == null || mAllContainersListString.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(true);*/

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);

        mMap.clear();


        FetchContainersTypeViewModelFactory factory = new FetchContainersTypeViewModelFactory(getApplication(), trashTypeSelected, mListPlacesString);
        FetchContainersTypeViewModel viewModel = ViewModelProviders.of(MapsActivity.this, factory)
                .get(FetchContainersTypeViewModel.class);

        viewModel.FetchContainersType(trashTypeSelected, mListPlacesString).observe(MapsActivity.this, new Observer<List<Container>>() {

            @Override
            public void onChanged(List<Container> containers) {

                mClusterManager.clearItems();
                mClusterManager.addItems(getContainersLocation(containers));
                mClusterManager.cluster();

            }
        });
        mProgressBar.setVisibility(View.GONE);

           /* GetDataService service = APIClient.getClient().create(GetDataService.class);

            if (mListPlacesString == null || mListPlacesString.isEmpty()) {

                Call<List<Place>> call1 = service.getAllPlaces();
                call1.enqueue(new Callback<List<Place>>() {
                    @Override
                    public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                        mListPlaces = response.body();
                        Gson gson = new Gson();
                        mListPlacesString = gson.toJson(mListPlaces);
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
                    Gson gson = new Gson();
                    mAllContainersListString = gson.toJson(allContainersList);

                    //covert list of places with coordinates into array map
                    ArrayMap<String, Place> allPlacesMap = new ArrayMap<String, Place>();
                    for (Place place : mListPlaces) {
                        allPlacesMap.put(place.getPlaceId(), place);
                    }

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
                            String title = selectedPlace.getTitle();

                            int binId = container.getBinId();
                            String underground = container.getUnderground();
                            String cleaning = container.getCleaning();
                            int progress = container.getProgress();

                            Container containerCoordinates = new Container(placeId, trashType, binId, underground, cleaning, progress, lat, lng, title);
                            containerCoordinatesList.add(containerCoordinates);
                        }
                    }*/

                    /*mClusterManager.clearItems();
                    mClusterManager.addItems(getContainersLocation(containerCoordinatesList));
                    mClusterManager.cluster();*/

              /*      mProgressBar.setVisibility(View.GONE);
                }*/

               /* @Override
                public void onFailure(Call<List<Container>> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                }
            });*/

       /* } else {
            mMap.clear();
            Gson gson = new Gson();
            Type typeContainer = new TypeToken<List<Container>>() {
            }.getType();
            List<Container> allContainersList = gson.fromJson(mAllContainersListString, typeContainer);

            //covert list of places with coordinates into array map
            ArrayMap<String, Place> allPlacesMap = new ArrayMap<String, Place>();
            Type typePlace = new TypeToken<List<Place>>() {
            }.getType();
            mListPlaces = gson.fromJson(mListPlacesString, typePlace);

            for (Place place : mListPlaces) {
                allPlacesMap.put(place.getPlaceId(), place);
            }

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
                    String title = selectedPlace.getTitle();

                    int binId = container.getBinId();
                    String underground = container.getUnderground();
                    String cleaning = container.getCleaning();
                    int progress = container.getProgress();

                    Container containerCoordinates = new Container(placeId, trashType, binId, underground, cleaning, progress, lat, lng, title);
                    containerCoordinatesList.add(containerCoordinates);
                }
            }
            mClusterManager.clearItems();
            mClusterManager.addItems(getContainersLocation(containerCoordinatesList));
            mClusterManager.cluster();

            mProgressBar.setVisibility(View.GONE);

        }*/


    }

    // the API call was already done and data about containers fetched and saved.
/*        else

    {

        Gson gson = new Gson();
        Type type = new TypeToken<List<Container>>() {
        }.getType();
        List<Container> containerCoordinatesList = gson.fromJson(mContainerCoordinatesListString, type);
        mClusterManager.clearItems();
        mClusterManager.addItems(getContainersLocation(containerCoordinatesList));
        mClusterManager.cluster();
    }

}*/

    // helper method to get clusteritems for container of selected type
    private List<TrashbinClusterItem> getContainersLocation(List<Container> containers) {
        List<TrashbinClusterItem> ListItems = new ArrayList<>();

        int locationCount = containers.size();
        String title = "";
        double lat = 0;
        double lng = 0;
        String placeId = "";

        for (int i = 0; i < locationCount; i++) {
            Container container = containers.get(i);

            lat = container.getLatitude();
            lng = container.getLongitude();
            title = container.getTitle();
            placeId = String.valueOf(container.getPlaceId());

            // Adding the marker to the List
            ListItems.add(new TrashbinClusterItem(lat, lng, title, placeId));
        }
        return ListItems;
    }
}



