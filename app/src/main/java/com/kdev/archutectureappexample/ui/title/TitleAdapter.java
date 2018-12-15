package com.kdev.archutectureappexample.ui.title;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kdev.archutectureappexample.R;
import com.kdev.archutectureappexample.data.model.db.Note;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TitleAdapter extends ListAdapter<Note, TitleAdapter.TitleViewHolder> {

    private OnItemClickListener listener;

    public TitleAdapter() {
        super(DIFF_CALLBACK);
    }

    private final static DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note> (){

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new TitleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    public Note getCurrentItem(int position){
        return getItem(position);
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
                    listener.onItemClick(getItem(position));
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
