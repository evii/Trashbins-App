package cz.optimization.odpadky.ui;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.CollapsingToolbarLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.room_data.PlaceWatched;
import cz.optimization.odpadky.room_data.PlacesDatabase;
import cz.optimization.odpadky.room_data.PlacesWatchedViewModel;
import cz.optimization.odpadky.room_data.DialogRecyclerViewAdapter;

import static android.view.View.GONE;

public class DetailActivity extends AppCompatActivity implements PlacesWatchedDialogFragment.NoticeDialogListener {
    private AdView mAdView;

    private String mPlaceId;
    private String title;
    private String containersListString;

    private PlacesDatabase placesDatabase;

    private DetailActivityRecyclerViewAdapter detailActivityAdapter;

    private PlaceWatched mPlaceWatched;
    private RecyclerView mDetailRecyclerView;

    private Dialog dialog;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDelete;

    private SharedPreferences preferences;


    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // gets data from MapsActivity as parent activity

            Intent intent = getIntent();
            mPlaceId = intent.getStringExtra("placeId");
            title = intent.getStringExtra("title");
            containersListString = intent.getStringExtra("containersList");



        Type type = new TypeToken<List<Container>>() {
        }.getType();
        Gson gson = new Gson();
        List<Container> containersList = gson.fromJson(containersListString, type);

        //recyclerview for containers
        mDetailRecyclerView = findViewById(R.id.detail_recycler_view);
        mDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailActivityAdapter = new DetailActivityRecyclerViewAdapter(containersList);

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

    // Watched Places
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    @Override
    public void onDialogClick(PlaceWatched selectedPlaceWatched) {
        String placeId = selectedPlaceWatched.getPlaceId();
        new QueryOnePlaceAsync().execute(placeId);
        mPlaceId = placeId;
        title = selectedPlaceWatched.getTitle();
        containersListString = selectedPlaceWatched.getContainersList();
    }

    @Override
    public void onDialogClickCheck(PlaceWatched selectedPlaceWatched) {
        String placeId = selectedPlaceWatched.getPlaceId();
        new QueryCheckPlacesAsync().execute(placeId);

    }

    // helper method for insert into db Watched Places
    private class InsertPlaceToWatchedAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
    public class QueryCheckPlacesAsync extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected PlaceWatched doInBackground(String... params) {
            // Query db for one place
            String placeId = params[0];
            PlaceWatched placeWatched = placesDatabase.placesWatchedModel().fetchOnePlacebyPlaceId(placeId);
            return placeWatched;
        }

        @Override
        protected void onPostExecute(PlaceWatched placeWatched) {
            if(placeWatched != null) {
                String watchedContainersListString = placeWatched.getContainersList();

                Type type = new TypeToken<List<Container>>() {
                }.getType();
                Gson gson = new Gson();
                List<Container> watchedContainersList = gson.fromJson(watchedContainersListString, type);

                detailActivityAdapter = new DetailActivityRecyclerViewAdapter(watchedContainersList);
                mDetailRecyclerView.setAdapter(detailActivityAdapter);

                String title = placeWatched.getTitle();
                collapsingToolbar.setTitle(title);
            }
            else {
                Log.d(TAG, "Place selected not contained in db");
            }
        }
    }

}
