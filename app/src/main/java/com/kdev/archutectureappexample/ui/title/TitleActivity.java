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
import com.kdev.archutectureappexample.ui.addNote.AddEditNoteActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;
import static com.kdev.archutectureappexample.utils.AppConstants.ADD_NOTE_REQUEST;
import static com.kdev.archutectureappexample.utils.AppConstants.DEFAULT_VALUE;
import static com.kdev.archutectureappexample.utils.AppConstants.EDIT_NOTE_REQUEST;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_DESCRIPTION;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_ID;
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
        viewModel.getAllNotes().observe(this, notes -> adapter.submitList(notes));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title, description;
        int id, priority;

        if(resultCode == RESULT_OK) {
            title = data.getStringExtra(EXTRA_TITLE);
            description = data.getStringExtra(EXTRA_DESCRIPTION);
            priority = data.getIntExtra(EXTRA_PRIORITY, DEFAULT_VALUE);

            if (requestCode == ADD_NOTE_REQUEST) {
                viewModel.insertNote(new Note(title, description, priority));
                Toast.makeText(this, getString(R.string.titleNoteCreated), Toast.LENGTH_SHORT).show();
            } else if (requestCode == EDIT_NOTE_REQUEST) {
                id = data.getIntExtra(EXTRA_ID, DEFAULT_VALUE);
                Note note = new Note(title, description, priority);
                note.setId(id);
                viewModel.updateNote(note);
                Toast.makeText(this, getString(R.string.titleNoteUpdated), Toast.LENGTH_SHORT).show();
            }
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

    private void bindView(){
        recyclerView = findViewById(R.id.titleRecV);
        fab = findViewById(R.id.titleFab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(TitleActivity.this, AddEditNoteActivity.class);
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

        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(TitleActivity.this, AddEditNoteActivity.class);
            intent.putExtra(EXTRA_ID, note.getId());
            intent.putExtra(EXTRA_TITLE, note.getTitle());
            intent.putExtra(EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(EXTRA_PRIORITY, note.getPriority());

            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        });
    }
}
