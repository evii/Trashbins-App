package cz.optimization.odpadky.objects;

import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("id")
    private final String mPlaceId;

    @SerializedName("title")
    private final String mTitle;

    @SerializedName("latitude")
    private final double mLatitude;

    @SerializedName("longitude")
    private final double mLongitude;


    public Place(String placeId, String title, double lat, double lng) {
        mPlaceId = placeId;
        mTitle = title;
        mLatitude = lat;
        mLongitude = lng;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public String getTitle() {
        return mTitle;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
