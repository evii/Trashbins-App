package cz.optimization.odpadky.room_data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

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

}