package com.architecturecomponentretrofit.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.architecturecomponentretrofit.room.Word;
import com.architecturecomponentretrofit.room.WordDao;
import com.architecturecomponentretrofit.room.WordRoomDatabase;

import java.util.List;

public class WordRepository {

    private WordDao wordDao;

    private LiveData<List<Word>>  mAllWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);//initialize db
        wordDao = db.wordDao(); // call create table
        mAllWords = wordDao.getAllWords(); // fetch data from db
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }
    public void insert (Word word) {
        new insertAsyncTask(wordDao).execute(word);
    }

    public void deleteWord (Word word) {
        new deleteAsyncTask(wordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        deleteAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.deleteWord(params[0]);
            return null;
        }
    }
}
