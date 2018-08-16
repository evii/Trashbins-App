package cz.optimization.odpadky.room_data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PlaceWatched.class}, version = 1)
public abstract class PlacesDatabase extends RoomDatabase {

    private static PlacesDatabase INSTANCE;

    public static PlacesDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), PlacesDatabase.class, "places_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract PlaceWatchedDao placesWatchedModel();

}
