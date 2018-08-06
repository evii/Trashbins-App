package cz.optimization.odpadky.room_data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PlaceWatched {

    @NonNull
    @PrimaryKey
    private String mPlaceId;
    private String mTitle;
    private String mContainersList;

    public PlaceWatched(String placeId, String title, String containersList) {
        mPlaceId = placeId;
        mTitle = title;
        mContainersList = containersList;
    }

    public String getPlaceId() { return mPlaceId; }
    public void setPlaceId(String placeId) { mPlaceId = placeId; }
    public String getTitle() { return mTitle; }
    public void setPlaceTitle (String title) { mTitle = title; }
    public String getContainersList() { return mContainersList; }
    public void setContainersList (String containersList) { mContainersList = containersList; }

}
