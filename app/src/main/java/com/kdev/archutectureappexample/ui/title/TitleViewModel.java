package com.kdev.archutectureappexample.ui.title;

import android.app.Application;

import com.kdev.archutectureappexample.data.model.db.Note;
import com.kdev.archutectureappexample.data.service.repository.NoteRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TitleViewModel extends AndroidViewModel {

    NoteRepository repository;


    public TitleViewModel(@NonNull Application application) {
        super(application);

        repository = new NoteRepository(application);
    }

    public void insertNote(Note note){
        repository.insert(note);
    }

    public void deleteNote(Note note){
        repository.delete(note);
    }

    public void updateNote(Note note){
        repository.update(note);
    }

    public void deleteAllNotes(){
        repository.deleteAll();
    }

    public LiveData<List<Note>> getAllNotes(){
        return repository.getAllNotes();
    }

}
