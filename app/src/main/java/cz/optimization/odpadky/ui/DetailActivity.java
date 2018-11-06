package cz.optimization.odpadky.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.CollapsingToolbarLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.room_data.PlaceWatched;
import cz.optimization.odpadky.room_data.PlacesDatabase;

import static android.view.View.GONE;

public class DetailActivity extends AppCompatActivity implements PlacesWatchedDialogFragment.NoticeDialogListener {
    private AdView mAdView;

    private static String mPlaceId;
    private static String title;
    private static String containersListString;

    private static PlacesDatabase placesDatabase;

    private static DetailActivityRecyclerViewAdapter detailActivityAdapter;
    private RecyclerView mDetailRecyclerView;

    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDelete;

    private static final String PLACEID_KEY = "placeIdKey";
    private static final String TITLE_KEY = "titleKey";
    private static final String CONTAINERS_STRING_KEY = "containersStringKey";

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // gets data from MapsActivity as parent activity
        if (getIntent() != null) {
            Intent intent = getIntent();
            mPlaceId = intent.getStringExtra(MapsActivity.PLACEID_KEY);
            title = intent.getStringExtra(MapsActivity.TITLE_KEY);
            containersListString = intent.getStringExtra(MapsActivity.CONTAINERS_KEY);
        }

        // retains data after orientation change
        if (savedInstanceState != null) {
            mPlaceId = savedInstanceState.getString(PLACEID_KEY);
            title = savedInstanceState.getString(TITLE_KEY);
            containersListString = savedInstanceState.getString(CONTAINERS_STRING_KEY);
        }

        Type type = new TypeToken<List<Container>>() {
        }.getType();
        Gson gson = new Gson();
        List<Container> containersList = gson.fromJson(containersListString, type);

        //recyclerview for containers
        mDetailRecyclerView = findViewById(R.id.detail_recycler_view);
        mDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailActivityAdapter = new DetailActivityRecyclerViewAdapter(containersList, this);

        mDetailRecyclerView.setAdapter(detailActivityAdapter);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // set layout with toolbars
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(title);

        // build Room db
        placesDatabase = Room.databaseBuilder(getApplicationContext(),
                PlacesDatabase.class, "places_db").build();

        // FAB setup
        fabAdd = findViewById(R.id.fab_add);
        fabDelete = findViewById(R.id.fab_delete);
        new QueryCheckPlacesAsync().execute(mPlaceId);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new InsertPlaceToWatchedAsync().execute();

                Snackbar.make(view, R.string.Action_add_to_watch, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceWatched placeWatchedtoDelete = new PlaceWatched(mPlaceId, title, containersListString);

                new DeletePlaceAsync().execute(placeWatchedtoDelete);

                Snackbar.make(view, R.string.Action_delete_from_watch, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //saving data from detail Activity before orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PLACEID_KEY, mPlaceId);
        outState.putString(TITLE_KEY, title);
        outState.putString(CONTAINERS_STRING_KEY, containersListString);
    }

    // restoring data from detail Activity after orientation change
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPlaceId = savedInstanceState.getString(PLACEID_KEY);
        title = savedInstanceState.getString(TITLE_KEY);
        containersListString = savedInstanceState.getString(CONTAINERS_STRING_KEY);
    }

    // Watched Places
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_watched) {
            showAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PlacesWatchedDialogFragment dialog = PlacesWatchedDialogFragment.newInstance("DDD");
        dialog.show(fm, "fragment_alert");
    }

    // method that handles click on watched place in dialog
    @Override
    public void onDialogClick(PlaceWatched selectedPlaceWatched) {
        String placeId = selectedPlaceWatched.getPlaceId();
        new QueryOnePlaceAsync().execute(placeId);
        mPlaceId = placeId;
        title = selectedPlaceWatched.getTitle();
        containersListString = selectedPlaceWatched.getContainersList();
    }

    // interface that checks if the place clicked on in dialog is in watched places.
    @Override
    public void onDialogClickCheck(PlaceWatched selectedPlaceWatched) {
        String placeId = selectedPlaceWatched.getPlaceId();
        new QueryCheckPlacesAsync().execute(placeId);
    }

    // helper method for insert into db Watched Places
    private class InsertPlaceToWatchedAsync extends AsyncTask<Void, Void, Void> {

        private PlaceWatched mPlaceWatched;

        @Override
        protected Void doInBackground(Void... voids) {
            //Add place to db
            mPlaceWatched = new PlaceWatched(mPlaceId, title, containersListString);
            placesDatabase.placesWatchedModel().insertSinglePlace(mPlaceWatched);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fabAdd.setVisibility(GONE);
            fabDelete.setVisibility(View.VISIBLE);
        }
    }

    // helper method for check if the place is in the DB - for the FAB to change
    private class QueryCheckPlacesAsync extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // Query db for one place
            String placeId = params[0];
            Boolean isInWatched;
            if (placesDatabase.placesWatchedModel().fetchOnePlacebyPlaceId(placeId) != null) {

                isInWatched = true;
            } else {
                isInWatched = false;
            }
            Log.v("isInWatched", String.valueOf(isInWatched));

            return isInWatched;
        }

        @Override
        protected void onPostExecute(Boolean isInWatched) {
            if (isInWatched) {
                fabAdd.setVisibility(GONE);
                fabDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    // helper method for deleting the place from db
    private class DeletePlaceAsync extends AsyncTask<PlaceWatched, Void, Void> {

        @Override
        protected Void doInBackground(PlaceWatched... placesWatched) {

            PlaceWatched placeWatched = placesWatched[0];
            placesDatabase.placesWatchedModel().deletePlace(placeWatched);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fabAdd.setVisibility(View.VISIBLE);
            fabDelete.setVisibility(GONE);
        }
    }

    // helper method for db Watched Places query - for one item - return it in Detail Activity layout
    private class QueryOnePlaceAsync extends AsyncTask<String, Void, PlaceWatched> {

        @Override
        protected PlaceWatched doInBackground(String... params) {
            // Query db for one place
            String placeId = params[0];
            PlaceWatched placeWatched = placesDatabase.placesWatchedModel().fetchOnePlacebyPlaceId(placeId);
            return placeWatched;
        }

        @Override
        protected void onPostExecute(PlaceWatched placeWatched) {
            if (placeWatched != null) {
                containersListString = placeWatched.getContainersList();

                Type type = new TypeToken<List<Container>>() {
                }.getType();
                Gson gson = new Gson();
                List<Container> watchedContainersList = gson.fromJson(containersListString, type);

                detailActivityAdapter = new DetailActivityRecyclerViewAdapter(watchedContainersList, getBaseContext());
                mDetailRecyclerView.setAdapter(detailActivityAdapter);

                title = placeWatched.getTitle();
                collapsingToolbar.setTitle(title);
            } else {
                Log.d(TAG, "Place selected not contained in db");
            }
        }
    }
}
