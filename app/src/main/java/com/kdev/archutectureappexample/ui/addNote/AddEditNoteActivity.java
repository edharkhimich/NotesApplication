package com.kdev.archutectureappexample.ui.addNote;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.kdev.archutectureappexample.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import static com.kdev.archutectureappexample.utils.AppConstants.DEFAULT_ID;
import static com.kdev.archutectureappexample.utils.AppConstants.DEFAULT_PRIORITY;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_DESCRIPTION;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_ID;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_PRIORITY;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_TITLE;
import static com.kdev.archutectureappexample.utils.AppConstants.NUMBER_PICKER_MAX_VALUE;
import static com.kdev.archutectureappexample.utils.AppConstants.NUMBER_PICKER_MIN_VALUE;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText titleEdTxt;
    private EditText descEdTxt;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        bindView();
    }

    private void bindView() {
        titleEdTxt = findViewById(R.id.addNoteTitleEdTxt);
        descEdTxt = findViewById(R.id.addNoteDescEdTxt);
        numberPicker = findViewById(R.id.addNoteNumberPicker);

        numberPicker.setMinValue(NUMBER_PICKER_MIN_VALUE);
        numberPicker.setMaxValue(NUMBER_PICKER_MAX_VALUE);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        checkIntent();
    }

    private void checkIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(R.string.addNoteEditNote);
            titleEdTxt.setText(intent.getStringExtra(EXTRA_TITLE));
            descEdTxt.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY, DEFAULT_PRIORITY));
        } else {
            setTitle(R.string.addNoteToolbarTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = titleEdTxt.getText().toString();
        String description = descEdTxt.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.addNoteFillUpAllFields), Toast.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_PRIORITY, priority);

            int id = getIntent().getIntExtra(EXTRA_ID, DEFAULT_ID);

            if (id != DEFAULT_ID) {
                data.putExtra(EXTRA_ID, id);
            }

            setResult(RESULT_OK, data);

            finish();

        }
    }
}
