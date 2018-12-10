package com.kdev.archutectureappexample.data.local.db;

import android.content.Context;
import android.os.AsyncTask;

import com.kdev.archutectureappexample.data.local.db.dao.NoteDao;
import com.kdev.archutectureappexample.data.model.db.Note;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static com.kdev.archutectureappexample.utils.AppConstants.DATABASE_NAME;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao getNoteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance).execute();
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        public PopulateAsyncTask(NoteDatabase db) {
            this.noteDao = db.getNoteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Desc 1", 1));
            noteDao.insert(new Note("Title 2", "Desc 2", 2));
            noteDao.insert(new Note("Title 3", "Desc 3", 3));
            return null;
        }
    }
}

