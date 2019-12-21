package com.example.xyzreader.ui.article_detail;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xyzreader.data.XYZRepository;
import com.example.xyzreader.database.XYZDatabase;
import com.example.xyzreader.domain.Book;

public class ArticleDetailViewModel extends AndroidViewModel {

    private static final String TAG = ArticleDetailViewModel.class.getSimpleName();
    private final XYZRepository repository;

    public ArticleDetailViewModel(Application application) {
        super(application);
        XYZDatabase database = XYZDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the recipes from the DataBase");
        repository = new XYZRepository(database);
    }

    LiveData<Integer> getNumberBook() {
        return repository.getNumberBook();
    }

    LiveData<Book> getBook(int id) {
        return repository.getBook(id);
    }

}
