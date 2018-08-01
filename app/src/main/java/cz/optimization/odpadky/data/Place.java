package cz.optimization.odpadky.data;

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

    @SerializedName("containers_count")
    private final int mContainersCount;

    public Place(String placeId, String title, double lat, double lng, int containersCount) {
        mPlaceId = placeId;
        mTitle = title;
        mLatitude = lat;
        mLongitude = lng;
        mContainersCount = containersCount;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public int getContainersCount() {
        return mContainersCount;
    }
}
