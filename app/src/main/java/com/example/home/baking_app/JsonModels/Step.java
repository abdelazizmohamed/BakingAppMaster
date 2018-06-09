package com.example.home.baking_app.JsonModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Step implements Parcelable {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("shortDescription")
    @Expose
    String shortDescription;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("videoURL")
    @Expose
    String videoURL;

    @SerializedName("thumbnailURL")
    @Expose
    String thumbnailURL;

    public Step(int id, String shortDescription, String description, String videoURL, String imageUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() {
        return this.id;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVideoURL() {
        return this.videoURL;
    }

    public String getThumbnailURL() {
        return this.thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
