package cz.optimization.odpadky.room_data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PlaceWatchedDao {

        @Insert(onConflict = REPLACE)
        void insertSinglePlace (PlaceWatched placeWatched);

        @Insert
        void insertMultiplePlaces (List<PlaceWatched> placesList);

        @Query("select * from PlaceWatched WHERE mPlaceId = :placeId")
        PlaceWatched fetchOnePlacebyPlaceId (int placeId);

        @Query("select * from PlaceWatched")
        LiveData<List<PlaceWatched>> fetchAllPlaces();

        @Update
        void updatePlace (PlaceWatched placeWatched);

        @Delete
        void deletePlace (PlaceWatched placeWatched);
    }


