package com.tripplanner.previous_trip_details;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.previous_trip.TripAdapter;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHandler>  {
    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
        Log.d("adpter", "NoteAdapter: "+noteList);

    }
    public void setNoteList(List<Note> noteList)
    {
        this.noteList = noteList;
        notifyDataSetChanged();
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
        holder.imageView_delete.setVisibility(View.INVISIBLE);
        if(note.isChecked()) {
            holder.chip.setText("Done");
        }
        else
        {
            holder.chip.setText("Canceled");
        }


    }



    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NoteViewHandler extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView_delete;
        Chip chip;
        public NoteViewHandler(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.note_name);
            imageView_delete=itemView.findViewById(R.id.delete_icon);
            chip=itemView.findViewById(R.id.done_chip);

        }
    }
}
