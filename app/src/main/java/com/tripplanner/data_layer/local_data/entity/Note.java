package com.tripplanner.data_layer.local_data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String noteName;
    private long tripId;
    private boolean checked;

    public Note(long id, String noteName, long tripId,boolean checked) {

        this.id = id;
        this.noteName = noteName;
        this.tripId = tripId;
        this.checked=checked;
    }
    @Ignore
    public Note( String noteName, int tripId,boolean checked) {
        this.noteName = noteName;
        this.tripId = tripId;
        this.checked=checked;
    }

    @Ignore
    public Note() {
    }


    protected Note(Parcel in) {
        id = in.readLong();
        noteName = in.readString();
        tripId = in.readLong();
        checked = in.readByte() != 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(noteName);
        parcel.writeLong(tripId);
        parcel.writeByte((byte) (checked ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteName='" + noteName + '\'' +
                ", tripId=" + tripId +
                ", checked=" + checked +
                '}';
    }
}
