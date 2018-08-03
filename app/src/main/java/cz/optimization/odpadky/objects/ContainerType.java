package cz.optimization.odpadky.objects;

import com.google.gson.annotations.SerializedName;

public class ContainerType {

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

    public ContainerType(String placeId, String trashType, int binId, String underground, String cleaning, int progress) {
        mPlaceId = placeId;
        mTrashType = trashType;
        mBinId = binId;
        mUnderground = underground;
        mCleaning = cleaning;
        mProgress = progress;
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
}
