package cz.optimization.odpadky.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Place implements Parcelable{

    @SerializedName("id")
    private final String mPlaceId;

    @SerializedName("title")
    private final String mTitle;

    @SerializedName("latitude")
    private final double mLatitude;

    @SerializedName("longitude")
    private final double mLongitude;


    public Place(String placeId, String title, double lat, double lng) {
        mPlaceId = placeId;
        mTitle = title;
        mLatitude = lat;
        mLongitude = lng;
    }

    protected Place(Parcel in) {
        mPlaceId = in.readString();
        mTitle = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getPlaceId() {
        return mPlaceId;
    }

    public String getTitle() {
        return mTitle;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPlaceId);
        parcel.writeString(mTitle);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
    }
}
