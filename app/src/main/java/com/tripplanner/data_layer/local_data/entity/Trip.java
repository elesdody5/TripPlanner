package com.tripplanner.data_layer.local_data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.firebase.firestore.ServerTimestamp;
import com.tripplanner.data_layer.local_data.DateTimeConverter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trip_table")
public class Trip implements Parcelable {




    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String userId;
    private String name;
    @Embedded(prefix = "start")
    private Place startPoint;
    @Embedded(prefix = "end")
    private Place endPoint;
    private boolean tripType;
    private  int tripStatus;

    @TypeConverters({DateTimeConverter.class})
    @ServerTimestamp
    private Date tripDate;
    @Ignore
    private List<Note> notes;
    private boolean online;




    public Trip(long id, String userId, String name, Place startPoint, Place endPoint, boolean tripType, int tripStatus, Date tripDate, boolean online) {
        this.id = id;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.tripType = tripType;
        this.tripStatus = tripStatus;
        this.tripDate = tripDate;
        this.userId=userId;
        this.online = online;
    }
    @Ignore
    public Trip(String userId,String name, Place startPoint, Place endPoint, boolean tripType, int tripStatus, Date tripDate,boolean online) {
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.tripType = tripType;
        this.tripStatus = tripStatus;
        this.tripDate = tripDate;
        this.userId=userId;
        notes=new ArrayList<>();
        this.online = online;
    }
    @Ignore
    public Trip() {
    }


    protected Trip(Parcel in) {
        id = in.readLong();
        userId = in.readString();
        name = in.readString();
        tripType = in.readByte() != 0;
        tripStatus = in.readInt();
        online = in.readByte() != 0;
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Place getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Place startPoint) {
        this.startPoint = startPoint;
    }

    public Place getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Place endPoint) {
        this.endPoint = endPoint;
    }

    public boolean isTripType() {
        return tripType;
    }

    public void setTripType(boolean tripType) {
        this.tripType = tripType;
    }

    public int getTripStatus() {

        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getUserId() {
        return userId;
    }
    public boolean isOnline() {
        return online;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", tripType=" + tripType +
                ", tripStatus=" + tripStatus +
                ", tripDate=" + tripDate +
                ", notes=" + notes +
                ", online=" + online +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(userId);
        parcel.writeString(name);
        parcel.writeByte((byte) (tripType ? 1 : 0));
        parcel.writeInt(tripStatus);
        parcel.writeByte((byte) (online ? 1 : 0));
    }
}