package com.architecturecomponentretrofit.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;

    //  constructor that gets a reference to the repository and gets the list of words from the repository.
    public WordViewModel(@NonNull Application application) {
        super(application);

        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }
    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }
//Create a wrapper insert() method that calls the Repository's insert() method.
    public void insert(Word word) {
        mRepository.insert(word);
    }

    public void deleteWord(Word word) {
        mRepository.deleteWord(word);
    }

}
