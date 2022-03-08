package com.azubal.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.azubal.go4lunch.models.ApiDetails.OpeningHours;
import com.google.android.gms.maps.model.LatLng;

public class Restaurant implements Parcelable {

    private String id;
    String name;
    String address;
    double rating;
    String distance;
    String formatted_address;
    String formatted_phone_number;
    OpeningHours openingHours ;
    String photoUrl ;
    String website;
    Double latitude;
    Double longitude;


    public Restaurant(String id, String name, double rating, String distance, String formatted_address, String formatted_phone_number,Double latitude,Double longitude, OpeningHours openingHours, String photoUrl,String website){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.distance = distance;
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openingHours = openingHours;
        this.photoUrl = photoUrl;
        this.website = website;
    }

    public Restaurant(){}


    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        rating = in.readDouble();
        distance = in.readString();
        formatted_address = in.readString();
        formatted_phone_number = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        photoUrl = in.readString();
        website = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(rating);
        dest.writeString(distance);
        dest.writeString(formatted_address);
        dest.writeString(formatted_phone_number);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(photoUrl);
        dest.writeString(website);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public double getRating() {
            return rating;
        }

        public String getDistance() {
            return distance;
        }

        public String getFormatted_address(){return formatted_address;}

        public void setFormatted_address(String formatted_address){this.formatted_address = formatted_address;}

        public String getFormatted_phone_number(){return  formatted_phone_number;}

        public void setFormatted_phone_number(String formatted_phone_number){this.formatted_phone_number = formatted_phone_number;}

        public OpeningHours getOpeningHours(){return openingHours;}

        public String getPhotoUrl(){return photoUrl;}

        public String getWebsite(){return website;}

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
