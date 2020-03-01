package com.tripplanner.previous_trip_details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.previous_trip.TripAdapter;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHandler>  {
    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;

    }
    @NonNull
    @Override
    public NoteViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_view, parent, false);
        return new NoteAdapter.NoteViewHandler(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHandler holder, int position) {
        Note note = noteList.get(position);
        holder.textView.setText(note.getNoteName());

    }



    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NoteViewHandler extends RecyclerView.ViewHolder {
        TextView textView;
        public NoteViewHandler(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.note_name);
        }
    }
}
