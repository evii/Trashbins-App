package cz.optimization.odpadky.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Container {

   /* @SerializedName("title")
    private final String mTitle;

    @SerializedName("latitude")
    private final String mLatitude;

    @SerializedName("longitude")
    private final double mLongitude;

    @SerializedName("containers_count")
    private final int mContainersCount;*/

    @SerializedName("place_id")
    private final String mPlaceId;

    @SerializedName("trash_type")
    private final String mTrashType;

    @SerializedName("bin_id")
    private final int mBinId;

    @SerializedName("underground")
    private final String mUnderground;

    @SerializedName("cleaning")
    private final String mCleaning;

    @SerializedName("demo_progress")
    private final int mProgress;

    private double mLatitude;
    private double mLongitude;
    private String mTitle;



    public Container(String placeId, String trashType, int binId, String underground, String cleaning, int progress) {
        mPlaceId = placeId;
        mTrashType = trashType;
        mBinId = binId;
        mUnderground = underground;
        mCleaning = cleaning;
        mProgress = progress;
    }

    public Container(String placeId, String trashType, int binId, String underground, String cleaning, int progress, double lat, double lng, String title) {
        mPlaceId = placeId;
        mTrashType = trashType;
        mBinId = binId;
        mUnderground = underground;
        mCleaning = cleaning;
        mProgress = progress;
        mLatitude = lat;
        mLongitude = lng;
        mTitle = title;

    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public String getTrashType() {
        return mTrashType;
    }

    public int getBinId() {
        return mBinId;
    }

    public String getUnderground() {
        return mUnderground;
    }

    public String getCleaning() {
        return mCleaning;
    }

    public int getProgress() {
        return mProgress;
    }

    public double getLatitude() { return mLatitude; }

    public double getLongitude() { return mLongitude; }

    public void setLatitude(double lat) { lat = mLatitude; }

    public void setLongitude(double lng) { lng = mLongitude; }

    public String getTitle() {
        return mTitle;
    }


    public static class ContainersResult {
        private List<Container> containers;

        public List<Container> getResults() {
            return containers;
        }
    }

}
