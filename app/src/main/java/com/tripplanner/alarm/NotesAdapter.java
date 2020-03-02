package com.tripplanner.alarm;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.tripplanner.R;
import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.previous_trip.TripAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHandler> {
private List<Note> noteList;
    Map<String, Object> noteMap;
FloatingViewService floatingViewService;


public NotesAdapter(List<Note> noteList,FloatingViewService floatingViewService) {
        this.noteList = noteList;
        this.floatingViewService=floatingViewService;

        }
@NonNull
@Override
public NoteViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.note_item_notification, parent, false);
        return new NotesAdapter.NoteViewHandler(itemView);    }

@Override
public void onBindViewHolder(@NonNull NoteViewHandler holder, int position) {
        Note note = noteList.get(position);
        holder.checkBox.setText(note.getNoteName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                noteMap=new HashMap<>();
                noteMap.put("checked",b);
                floatingViewService.updateNote(note,noteMap);




            }
        });

        }



@Override
public int getItemCount() {
        return noteList.size();
        }

public class NoteViewHandler extends RecyclerView.ViewHolder {
    CheckBox checkBox;
    public NoteViewHandler(@NonNull View itemView) {
        super(itemView);
        checkBox=itemView.findViewById(R.id.check);
    }
}
}