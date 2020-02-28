package com.tripplanner.add_trip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.databinding.NoteItemViewBinding;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
   private ArrayList<Note> noteList;

    NoteAdapter(ArrayList<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        NoteItemViewBinding itemBinding =
                NoteItemViewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(noteList.get(position));
        holder.binding.deleteIcon.setOnClickListener(view -> {
            deleteItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    void addNote(Note note) {
        noteList.add(note);
        notifyDataSetChanged();
    }
    private void deleteItem(int postion)
    {
        noteList.remove(postion);
        notifyItemRemoved(postion);
    }

    ArrayList<Note> getNotes() {
        return noteList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final NoteItemViewBinding binding;

        ViewHolder(NoteItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Note note) {
            binding.setNote(note);
            binding.executePendingBindings();
        }
    }
}
