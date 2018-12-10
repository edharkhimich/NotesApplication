package com.kdev.archutectureappexample.ui.title;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kdev.archutectureappexample.R;
import com.kdev.archutectureappexample.data.model.db.Note;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TitleActivity extends AppCompatActivity {

    private TitleViewModel viewModel;
    private RecyclerView recyclerView;
    private TitleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        bindView();

        viewModel = ViewModelProviders.of(this).get(TitleViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
               adapter.setItems(notes);
            }
        });
    }

    private void bindView(){
        recyclerView = findViewById(R.id.titleRecV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TitleAdapter();
        recyclerView.setAdapter(adapter);
    }
}
