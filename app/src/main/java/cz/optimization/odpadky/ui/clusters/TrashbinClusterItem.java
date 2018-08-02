package cz.optimization.odpadky.ui.clusters;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by evi on 21. 1. 2018.
 */

public class TrashbinClusterItem implements ClusterItem {
    private final String mTitle;
    private final LatLng mPosition;
    private final String mPlaceId;
  //   private final String mPlaceId;


    public TrashbinClusterItem(double lat, double lng, String title, String placeId) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
     //   mSnippet = snippet;
        mPlaceId = placeId;

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

  /* public String getPLaceId() {
        return mPlaceId;
    }*/


}


