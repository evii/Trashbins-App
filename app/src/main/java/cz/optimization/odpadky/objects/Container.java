package cz.optimization.odpadky.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Container {

    @SerializedName("place_id")
    private final String mPlaceId;

    @SerializedName("trash_type")
    private final String mTrashType;

    @SerializedName("underground")
    private final String mUnderground;

    @SerializedName("cleaning")
    private final String mCleaning;

    @SerializedName("demo_progress")
    private final int mProgress;

    private double mLatitude;
    private double mLongitude;
    private String mTitle;


    public Container(String placeId, String trashType, String underground, String cleaning, int progress, double lat, double lng, String title) {
        mPlaceId = placeId;
        mTrashType = trashType;
        mUnderground = underground;
        mCleaning = cleaning;
        mProgress = progress;
        mLatitude = lat;
        mLongitude = lng;
        mTitle = title;
    }

    public Container(String placeId, String trashType, int progress, double lat, double lng) {
        mPlaceId = placeId;
        mTrashType = trashType;
        mProgress = progress;
        mLatitude = lat;
        mLongitude = lng;
        mUnderground = "";
        mCleaning = "";

    }


    public String getPlaceId() {
        return mPlaceId;
    }

    public String getTrashType() {
        return mTrashType;
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
