package com.kdev.archutectureappexample.ui.title;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kdev.archutectureappexample.R;
import com.kdev.archutectureappexample.data.model.db.Note;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.TitleViewHolder> {

    private List<Note> items = new ArrayList<>();
    private OnItemClickListener listener;

    public void setItems(List<Note> items){
        if (this.items.size() > 0){
            this.items.clear();
        }
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new TitleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Note getCurrentItem(int position){
        return items.get(position);
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTxtV;
        private TextView descTxtV;
        private TextView priority;

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxtV = itemView.findViewById(R.id.itemTitleTxtV);
            descTxtV = itemView.findViewById(R.id.itemDescriptionTxtV);
            priority = itemView.findViewById(R.id.itemPriorityTxtV);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(items.get(position));
                }
            });
        }

        void bind(Note note){
            titleTxtV.setText(note.getTitle());
            descTxtV.setText(note.getDescription());
            priority.setText(String.valueOf(note.getPriority()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
