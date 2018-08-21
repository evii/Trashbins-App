package cz.optimization.odpadky.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Container implements Parcelable{

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

    public Container(String placeId, String trashType, int progress, double lat, double lng, String title) {
        mPlaceId = placeId;
        mTrashType = trashType;
        mProgress = progress;
        mLatitude = lat;
        mLongitude = lng;
        mTitle = title;
        mUnderground = "";
        mCleaning = "";

    }


    protected Container(Parcel in) {
        mPlaceId = in.readString();
        mTrashType = in.readString();
        mUnderground = in.readString();
        mCleaning = in.readString();
        mProgress = in.readInt();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mTitle = in.readString();
    }

    public static final Creator<Container> CREATOR = new Creator<Container>() {
        @Override
        public Container createFromParcel(Parcel in) {
            return new Container(in);
        }

        @Override
        public Container[] newArray(int size) {
            return new Container[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPlaceId);
        parcel.writeString(mTrashType);
        parcel.writeString(mUnderground);
        parcel.writeString(mCleaning);
        parcel.writeInt(mProgress);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mTitle);
    }


    public static class ContainersResult {
        private List<Container> containers;

        public List<Container> getResults() {
            return containers;
        }
    }

}
