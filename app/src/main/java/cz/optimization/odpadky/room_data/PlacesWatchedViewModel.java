package cz.optimization.odpadky.room_data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class PlacesWatchedViewModel extends AndroidViewModel {

    private final LiveData<List<PlaceWatched>> placesList;

    private PlacesDatabase placesDatabase;

    public PlacesWatchedViewModel(Application application) {
        super(application);

        placesDatabase = PlacesDatabase.getDatabase(this.getApplication());

        placesList = placesDatabase.placesWatchedModel().fetchAllPlaces();
    }


    public LiveData<List<PlaceWatched>> getPlaceWatchedList() {
        return placesList;
    }

    public void deleteItem(PlaceWatched placeWatched) {
        new deleteAsyncTask(placesDatabase).execute(placeWatched);
    }

    private static class deleteAsyncTask extends AsyncTask<PlaceWatched, Void, Void> {

        private PlacesDatabase db;

        deleteAsyncTask(PlacesDatabase placesDatabase) {
            db = placesDatabase;
        }

        @Override
        protected Void doInBackground(final PlaceWatched... params) {
            db.placesWatchedModel().deletePlace(params[0]);
            return null;
        }

    }

}