package com.kdev.archutectureappexample.ui.title;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kdev.archutectureappexample.R;
import com.kdev.archutectureappexample.data.model.db.Note;
import com.kdev.archutectureappexample.ui.addNote.AddNoteActivity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.kdev.archutectureappexample.utils.AppConstants.ADD_NOTE_REQUEST;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_DESCRIPTION;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_PRIORITY;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_TITLE;

public class TitleActivity extends AppCompatActivity {

    private TitleViewModel viewModel;
    private RecyclerView recyclerView;
    private TitleAdapter adapter;
    private FloatingActionButton fab;

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(EXTRA_TITLE);
            String description = data.getStringExtra(EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(EXTRA_PRIORITY, 0);

            saveToDb(title, description, priority);
        } else {
            Toast.makeText(this, )
        }
    }

    private void saveToDb(String title, String desc, int priority){
        viewModel.insertNote(new Note(title, desc, priority));
    }

    private void bindView(){
        recyclerView = findViewById(R.id.titleRecV);
        fab = findViewById(R.id.titleFab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(TitleActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TitleAdapter();
        recyclerView.setAdapter(adapter);
    }
}
