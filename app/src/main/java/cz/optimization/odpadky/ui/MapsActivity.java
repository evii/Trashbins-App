package cz.optimization.odpadky.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.retrofit_data.APIClient;
import cz.optimization.odpadky.retrofit_data.ContainersViewModel;
import cz.optimization.odpadky.retrofit_data.GetDataService;
import cz.optimization.odpadky.objects.Place;
import cz.optimization.odpadky.retrofit_data.PlacesViewModel;
import cz.optimization.odpadky.ui.clusters.CustomClusterRenderer;
import cz.optimization.odpadky.ui.clusters.TrashbinClusterItem;
import cz.optimization.odpadky.ui.info_window.CustomInfoWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        SelectTrashbinTypeDialogFragment.AlertPositiveListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Gson gson;
    public static int selectedType = 0;
    private double mHomeLatitude;
    private double mHomeLongitude;
    private float mCameraZoom;

    private static final String POSITION_KEY = "selectedType";
    private static final String LAT_KEY = "latitude";
    private static final String LNG_KEY = "longitude";
    private static final String ZOOM_KEY = "zoom";
    private static final String INFOPLACEID_KEY = "infowindowplaceid";
    private static final String CLUSTERITEM_KEY = "clusteritem";
    private static final String INFOWINDOW_KEY = "infowindow";
    public static final String PLACEID_KEY = "placeId";
    public static final String TITLE_KEY = "title";
    public static final String CONTAINERS_KEY = "containersList";

    private int previousPosition;

    private ClusterManager<TrashbinClusterItem> mClusterManager;
    private List<Place> mListPlaces;
    private List<Container> mAllContainersList;

    private CustomClusterRenderer renderer;
    private TrashbinClusterItem trashbinClusterItem;

    public static final String PREFS_NAME = "Containers_object";
    public static final String PREFS_KEY = "Containers_key";
    private SharedPreferences sharedPreferences;

    private static final String TAG = "MapsActivity";

    private ProgressBar mProgressBar;
    private Marker mMarker;
    private Marker markerWithInfoWindowShown;

    private boolean isInfoDisplayed;
    private String mPlaceIdInfo;

    private PlacesViewModel placesViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();
        isInfoDisplayed = false;
        placesViewModel = ViewModelProviders.of(this).get(PlacesViewModel.class);

        //intial selectedType of map
        mHomeLatitude = 50.0889853530001;
        mHomeLongitude = 14.4723441130001;
        mCameraZoom = 15.5f;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // restoring selectedType and zoom on the map
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
            previousPosition = selectedType;
        }

        mProgressBar = findViewById(R.id.progress_bar);
    }

    //saving selectedType and zoom on the map
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_KEY, selectedType);

        LatLng mapPosition = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
        float zoom = mMap.getCameraPosition().zoom;
        double lat = mapPosition.latitude;
        double lng = mapPosition.longitude;
        outState.putFloat(ZOOM_KEY, zoom);
        outState.putDouble(LAT_KEY, lat);
        outState.putDouble(LNG_KEY, lng);
    }

    // restoring selectedType and zoom on the map
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHomeLatitude = savedInstanceState.getDouble(LAT_KEY);
        mHomeLongitude = savedInstanceState.getDouble(LNG_KEY);
        mCameraZoom = savedInstanceState.getFloat(ZOOM_KEY);

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
        isInfoDisplayed = false;

        if (id == R.id.action_filtr) {
            FragmentManager fm = getSupportFragmentManager();
            SelectTrashbinTypeDialogFragment dialog = new SelectTrashbinTypeDialogFragment();

            // Creating a bundle object to store the selected item's index
            Bundle b = new Bundle();

            // Storing the selected item's index in the bundle object
            b.putInt("selectedType", selectedType);

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
        Log.v("OnsaveLiveData", "onPositiveClick " + String.valueOf(previousPosition));

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
                    Log.v("OnsaveLiveData", "widget " + widgetClicked);
                    mMap.clear();
                    setTitle(R.string.all_title);
                    selectedType = 0;
                    fetchPlaces();
                    break;

                case TrashbinAppWidgetProvider.GLASS_BUTTON:
                    mMap.clear();
                    setTitle(R.string.glass_title);
                    selectedType = 1;
                    fetchContainersType(getResources().getString(R.string.glass));
                    break;

                case TrashbinAppWidgetProvider.CLEAR_GLASS_BUTTON:
                    mMap.clear();
                    setTitle(R.string.clear_glass_title);
                    selectedType = 2;
                    fetchContainersType(getResources().getString(R.string.clear_glass));
                    break;

                case TrashbinAppWidgetProvider.METAL_BUTTON:
                    mMap.clear();
                    setTitle(R.string.metal_title);
                    selectedType = 3;
                    fetchContainersType(getResources().getString(R.string.metal));
                    break;

                case TrashbinAppWidgetProvider.PLASTIC_BUTTON:
                    mMap.clear();
                    setTitle(R.string.plastic_title);
                    selectedType = 4;
                    fetchContainersType(getResources().getString(R.string.plastic));
                    break;

                case TrashbinAppWidgetProvider.PAPER_BUTTON:
                    mMap.clear();
                    setTitle(R.string.paper_title);
                    selectedType = 5;
                    fetchContainersType(getResources().getString(R.string.paper));
                    break;

                case TrashbinAppWidgetProvider.CARTON_BUTTON:
                    mMap.clear();
                    setTitle(R.string.carton_title);
                    selectedType = 6;
                    fetchContainersType(getResources().getString(R.string.carton));
                    break;

                case TrashbinAppWidgetProvider.ELECTRICAL_BUTTON:
                    mMap.clear();
                    setTitle(R.string.electrical_title);
                    selectedType = 7;
                    fetchContainersType(getResources().getString(R.string.electrical));
                    break;
            }
        }
    }

    //Filter in action bar
    @Override
    public void onPositiveClick(int position) {
        this.selectedType = position;

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

        intent.putExtra(PLACEID_KEY, placeId);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(CONTAINERS_KEY, containersList);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

///////////////////////////////////////
//     FETCH PLACES                  //
//////////////////////////////////////

    // helper method to get the places from the API - using retrofit + setting onclicklisteners on markers
    public void fetchPlaces() {
        mMap.clear();

        // call to API for places made for the first time
        if (mListPlaces == null) {

            //handle error when the data is not loaded
            placesViewModel.getError().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean error) {
                    if (error)
                        Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                }
            });

            //handle progressbar
            placesViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean isLoading) {
                    if (isLoading)
                        mProgressBar.setVisibility(View.VISIBLE);
                    else
                        mProgressBar.setVisibility(View.GONE);
                }
            });

            // fetch data about places
            placesViewModel.getPlaces().observe(this, new Observer<List<Place>>() {
                @Override
                public void onChanged(@Nullable List<Place> placesList) {
                    mListPlaces = placesList;
                    fetchPlacesHelper(placesList);
                }
            });

        } else {
            fetchPlacesHelper(mListPlaces);
        }
    }

    // helper method to avoid doubling the code - to fechtPlaces() method
    private void fetchPlacesHelper(List<Place> listPlaces) {

        mClusterManager.clearItems();
        List<TrashbinClusterItem> itemsToAdd = getPlaceLocation(listPlaces);
        mClusterManager.addItems(itemsToAdd);
        mClusterManager.cluster();

        //restore Info window if it was displayed before
        MarkerOptions markerOptions = null;

        if (isInfoDisplayed) {
            for (TrashbinClusterItem item : itemsToAdd) {
                String placeId = item.getSnippet();
                if (placeId.equals(mPlaceIdInfo)) {

                    trashbinClusterItem = item;
                    BitmapDescriptor color = assignMarkerColor(selectedType);
                    markerOptions = new MarkerOptions().position(item.getPosition())
                            .title(item.getTitle()).snippet(item.getSnippet()).icon(color);
                }
            }

            fetchContainersAtPlace(mPlaceIdInfo);
            markerWithInfoWindowShown = mMap.addMarker(markerOptions);
            markerWithInfoWindowShown.showInfoWindow();
        }

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
                        String placeId = clusterItem.getSnippet();

                        //get Containers for given placeId
                        fetchContainersAtPlace(placeId);

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

///////////////////////////////////////
//     FETCH CONTAINERS AT PLACE     //
//////////////////////////////////////

    // helper method to get details of containers in a place
    private void fetchContainersAtPlace(String placeId) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        Call<Container.ContainersResult> call = service.getContainersList(placeId);
        call.enqueue(new Callback<Container.ContainersResult>() {
            @Override
            public void onResponse(Call<Container.ContainersResult> call, Response<Container.ContainersResult> response) {

                List<Container> containers = response.body().getResults();

                String containersString = gson.toJson(containers);

                // Puting data about Containers at given place to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREFS_KEY, containersString);
                editor.commit();


                mMarker = renderer.getMarker(trashbinClusterItem);
                if (mMarker != null) {
                    mMarker.showInfoWindow();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Container.ContainersResult> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

///////////////////////////////////////
//     FETCH CONTAINERS BY TYPE     //
//////////////////////////////////////

    // helper method to display containers of selected type
    public void fetchContainersType(final String trashTypeSelected) {

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
        mMap.clear();

        GetDataService service = APIClient.getClient().create(GetDataService.class);

        if (mListPlaces == null) {
            mMap.clear();

            //handle error when the data is not loaded
            placesViewModel.getError().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean error) {
                    if (error)
                        Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                }
            });

            //handle progressbar
            placesViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean isLoading) {
                    if (isLoading)
                        mProgressBar.setVisibility(View.VISIBLE);
                    else
                        mProgressBar.setVisibility(View.GONE);
                }
            });

            placesViewModel.getPlaces().observe(this, new Observer<List<Place>>() {
                @Override
                public void onChanged(@Nullable List<Place> placesList) {
                    mListPlaces = placesList;
                    //  fetchPlacesHelper(placesList);
                    mProgressBar.setVisibility(View.GONE);
                    Log.v("OnsaveLiveData", "fetchContainersnull " + String.valueOf(mListPlaces.size()));

                }
            });
        }


        if (mAllContainersList == null) {

            mMap.clear();
            ContainersViewModel containersViewModel = ViewModelProviders.of(this).get(ContainersViewModel.class);

            //handle error when the data is not loaded
            containersViewModel.getError().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean error) {
                    if (error)
                        Toast.makeText(MapsActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                }
            });

            //handle progressbar
            containersViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean isLoading) {
                    if (isLoading)
                        mProgressBar.setVisibility(View.VISIBLE);
                    else
                        mProgressBar.setVisibility(View.GONE);
                }
            });

            containersViewModel.getContainers().observe(this, new Observer<List<Container>>() {
                @Override
                public void onChanged(@Nullable List<Container> containersList) {
                    mAllContainersList = containersList;
                    new AssignCoordinatesTask().execute(trashTypeSelected);

                }
            });

        } else {
            new AssignCoordinatesTask().execute(trashTypeSelected);
        }
    }

// helper method to assign coordinates to containers

    private class AssignCoordinatesTask extends AsyncTask<String, Void, List<Container>> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(true);
            super.onPreExecute();
        }

        @Override
        protected List<Container> doInBackground(String... params) {

            // create new containers list with coordinates
            List<Container> containerCoordinatesList = new ArrayList<Container>();
            String trashType = "";
            String trashTypeSel = params[0];

            final ArrayMap<String, Place> allPlacesMap;
            //convert list of places with coordinates into array map
            allPlacesMap = new ArrayMap<String, Place>();
            List<Place> placesList = mListPlaces;

            for (Place place : placesList) {
                allPlacesMap.put(place.getPlaceId(), place);
            }
            List<Container> allContainersList = mAllContainersList;
            for (Container container : allContainersList) {
                trashType = container.getTrashType();

                if (trashType.equals(trashTypeSel)) {

                    String placeId = container.getPlaceId();
                    Place selectedPlace = allPlacesMap.get(placeId);
                    double lat = selectedPlace.getLatitude();
                    double lng = selectedPlace.getLongitude();
                    String title = selectedPlace.getTitle();

                    int progress = container.getProgress();

                    Container containerCoordinates = new Container(placeId, trashType, progress, lat, lng, title);
                    containerCoordinatesList.add(containerCoordinates);
                }
            }

            return containerCoordinatesList;
        }

        @Override
        protected void onPostExecute(List<Container> containerCoordinatesList) {

            mClusterManager.clearItems();
            List<TrashbinClusterItem> itemsToAdd = getContainersLocation(containerCoordinatesList);
            mClusterManager.addItems(itemsToAdd);
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
                            String placeId = clusterItem.getSnippet();

                            //get Containers for given placeId
                            fetchContainersAtPlace(placeId);
                            Marker marker = renderer.getMarker(trashbinClusterItem);
                            marker.showInfoWindow();

                            return false;

                        }
                    });


            //restore Info window if it was displayed before

            MarkerOptions markerOptions = null;

            if (isInfoDisplayed) {
                for (TrashbinClusterItem item : itemsToAdd) {
                    String placeId = item.getSnippet();
                    if (placeId.equals(mPlaceIdInfo)) {

                        trashbinClusterItem = item;
                        BitmapDescriptor color = assignMarkerColor(selectedType);
                        markerOptions = new MarkerOptions().position(item.getPosition())
                                .title(item.getTitle()).snippet(item.getSnippet()).icon(color);
                    }
                }

                //get Containers for given placeId
                fetchContainersAtPlace(mPlaceIdInfo);
                if (markerOptions != null) {
                    markerWithInfoWindowShown = mMap.addMarker(markerOptions);
                    markerWithInfoWindowShown.showInfoWindow();
                }
            }
            mProgressBar.setVisibility(View.GONE);
        }

    }


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

    // helper method to choose right marker color

    private BitmapDescriptor assignMarkerColor(int selectedType) {

        //different markers color for each trash type
        BitmapDescriptor markerDescriptor;

        switch (selectedType) {
            // all
            case 0:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            // glass
            case 1:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                break;
            // clear glass
            case 2:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                break;
            // metal
            case 3:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                break;
            // plastic
            case 4:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            // paper
            case 5:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                break;
            // carton
            case 6:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                break;
            //electrical
            case 7:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                break;

            default:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
        }
        return markerDescriptor;
    }
}
