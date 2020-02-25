package com.tripplanner.data_layer.local_data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
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

}
