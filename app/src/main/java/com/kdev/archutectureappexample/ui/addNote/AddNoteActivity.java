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

import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_DESCRIPTION;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_PRIORITY;
import static com.kdev.archutectureappexample.utils.AppConstants.EXTRA_TITLE;

public class AddNoteActivity extends AppCompatActivity {

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

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle(R.string.addNoteToolbarTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveNote :
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

        if(title.trim().isEmpty()|| description.trim().isEmpty()){
            Toast.makeText(this, getString(R.string.fillUpAllFields), Toast.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_PRIORITY, priority);

            setResult(RESULT_OK, data);

            finish();
        }
    }
}
