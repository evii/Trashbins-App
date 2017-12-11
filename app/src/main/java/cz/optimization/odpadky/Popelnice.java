package cz.optimization.odpadky;

import static android.R.attr.type;

/**
 * Created by evi on 5. 12. 2017.
 */
public class Popelnice {
    private double mLat;
    private double mLong;
    private String mAddress;
    private String mType;
    private double mFullness;

    public Popelnice(double lat, double longt, String address, String type, double full) {
        mLat = lat;
        mLong = longt;
        mAddress = address;
        mType = type;
        mFullness = full;

    }

    public double getLat() {
        return mLat;
    }

    public double getLong() {
        return mLong;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getType() {
        return mType;
    }

    public double getFullness() {
        return mFullness;
    }


}
