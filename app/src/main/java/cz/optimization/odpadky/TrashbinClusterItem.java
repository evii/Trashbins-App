package cz.optimization.odpadky;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by evi on 21. 1. 2018.
 */

public class TrashbinClusterItem implements ClusterItem {

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

    private LatLng mPosition;

    public TrashbinClusterItem(String placeId, String title, double lat, double lng, int containersCount) {

        mPlaceId=placeId;
        mTitle = title;
        mLatitude=lat;
        mLongitude=lng;
        mContainersCount=containersCount;

        mPosition = new LatLng(lat, lng);
  }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mPlaceId;
    }

    public String getPlaceId() {
        return mPlaceId;
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

