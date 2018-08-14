package cz.optimization.odpadky.ui.clusters;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by evi on 21. 1. 2018.
 */

public class TrashbinClusterItem implements ClusterItem, Parcelable {
    private final String mTitle;
    private final LatLng mPosition;
    private final String mPlaceId;




    public TrashbinClusterItem(double lat, double lng, String title, String placeId) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mPlaceId = placeId;

    }

    protected TrashbinClusterItem(Parcel in) {
        mTitle = in.readString();
        mPosition = in.readParcelable(LatLng.class.getClassLoader());
        mPlaceId = in.readString();
    }

    public static final Creator<TrashbinClusterItem> CREATOR = new Creator<TrashbinClusterItem>() {
        @Override
        public TrashbinClusterItem createFromParcel(Parcel in) {
            return new TrashbinClusterItem(in);
        }

        @Override
        public TrashbinClusterItem[] newArray(int size) {
            return new TrashbinClusterItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeParcelable(mPosition, i);
        parcel.writeString(mPlaceId);
    }

  /* public String getPLaceId() {
        return mPlaceId;
    }*/


}


