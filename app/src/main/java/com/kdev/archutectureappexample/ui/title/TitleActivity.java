package com.kdev.archutectureappexample.ui.title;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kdev.archutectureappexample.R;
import com.kdev.archutectureappexample.data.model.db.Note;
import com.kdev.archutectureappexample.ui.addNote.AddNoteActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;
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
            Toast.makeText(this, getString(R.string.titleNoteNotSave), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.title_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.titleMenuDeleteAll :
                viewModel.deleteAllNotes();
                Toast.makeText(this, getString(R.string.titleAllNotesDeleted), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, LEFT
                | RIGHT) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ACTION_STATE_SWIPE, LEFT | RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.deleteNote(adapter.getCurrentItem(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
    }
}
