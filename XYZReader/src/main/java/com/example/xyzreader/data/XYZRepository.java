package com.example.xyzreader.data;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.xyzreader.database.DatabaseBook;
import com.example.xyzreader.database.XYZDatabase;
import com.example.xyzreader.domain.Book;
import com.example.xyzreader.remote.BooksTask;
import com.example.xyzreader.remote.RemoteListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XYZRepository {

    private final XYZDatabase mXYZDatabase;

    private static final String BASE_URL = "https://go.udacity.com/xyz-reader-json";

    public XYZRepository(XYZDatabase xyzDatabase) {
        mXYZDatabase = xyzDatabase;
    }

    public void refresh(RemoteListener remoteListener) {
        URL url = null;
        try {
            url = new URL(BASE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        new BooksTask(mXYZDatabase, remoteListener).execute(finalUrl);
    }

    public LiveData<List<Book>> getBooks() {
        return Transformations.map(mXYZDatabase.xyzDao().getBooks(),
                new Function<List<DatabaseBook>, List<Book>>() {
                    @Override
                    public List<Book> apply(List<DatabaseBook> databaseBooks) {
                        List<Book> books = new ArrayList<>();

                        for (int i = 0; i < databaseBooks.size(); i++) {
                            DatabaseBook databaseBook = databaseBooks.get(i);
                            books.add(
                                    new Book(
                                            databaseBook.getId(),
                                            databaseBook.getTitle(),
                                            databaseBook.getAuthor(),
                                            databaseBook.getBody(),
                                            databaseBook.getThumb(),
                                            databaseBook.getPhoto(),
                                            databaseBook.getAspect_ratio(),
                                            databaseBook.getPublished_date()
                                    )
                            );
                        }

                        return books;
                    }
                });
    }
}