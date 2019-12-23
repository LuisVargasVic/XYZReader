package com.example.xyzreader.presentation.articles;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xyzreader.data.XYZRepository;
import com.example.xyzreader.database.XYZDatabase;
import com.example.xyzreader.domain.Book;
import com.example.xyzreader.remote.RemoteListener;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {

    private static final String TAG = ArticleViewModel.class.getSimpleName();
    private final XYZRepository repository;
    private final LiveData<List<Book>> books;

    public ArticleViewModel(Application application) {
        super(application);
        XYZDatabase database = XYZDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the recipes from the DataBase");
        repository = new XYZRepository(database);
        books = repository.getBooks();
    }

    void refresh(RemoteListener remoteListener) {
        repository.refresh(remoteListener);
    }

    LiveData<List<Book>> getBooks() {
        return books;
    }

}
