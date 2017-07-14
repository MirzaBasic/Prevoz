package com.basic.prevoz.Models.GoogleMapsModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Developer on 17.06.2017..
 */


@SuppressWarnings("WeakerAccess")
public class GeocodedWaypoint implements Parcelable {
    @SerializedName("geocoder_status")
    private String status;
    @SerializedName("place_id")
    private String placeId;
    private List<String> types;

    public GeocodedWaypoint() {
    }

    protected GeocodedWaypoint(Parcel in) {
        status = in.readString();
        placeId = in.readString();
        types = in.createStringArrayList();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(placeId);
        dest.writeStringList(types);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeocodedWaypoint> CREATOR = new Creator<GeocodedWaypoint>() {
        @Override
        public GeocodedWaypoint createFromParcel(Parcel in) {
            return new GeocodedWaypoint(in);
        }

        @Override
        public GeocodedWaypoint[] newArray(int size) {
            return new GeocodedWaypoint[size];
        }
    };
}
