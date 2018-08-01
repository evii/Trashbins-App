package cz.optimization.odpadky;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by evi on 21. 1. 2018.
 */

    public class TrashbinClusterItem implements ClusterItem {
        private final String mTitle;
        private final LatLng mPosition;
        private final String mSnippet;

        public TrashbinClusterItem(double lat, double lng, String title, String snippet) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
            mSnippet = snippet;
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
            return mSnippet;
        }
    }


