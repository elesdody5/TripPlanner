package com.tripplanner.data_layer.local_data.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String noteName;
    private int tripId;
    private boolean checked;

    public Note(int id, String noteName, int tripId,boolean checked) {
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
}
