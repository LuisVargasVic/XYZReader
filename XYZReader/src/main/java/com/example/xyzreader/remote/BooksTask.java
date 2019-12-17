package com.example.xyzreader.remote;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xyzreader.database.DatabaseBook;
import com.example.xyzreader.database.XYZDatabase;
import com.example.xyzreader.domain.Book;
import com.example.xyzreader.utils.JsonUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BooksTask extends AsyncTask<URL, Void, Boolean> {

    private final XYZDatabase mXYZDatabase;
    private final RemoteListener mRemoteListener;
    private static final String TAG = BooksTask.class.getSimpleName();

    public BooksTask(XYZDatabase xyzDatabase, RemoteListener remoteListener) {
        mXYZDatabase = xyzDatabase;
        mRemoteListener = remoteListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRemoteListener.preExecute();
    }

    @Override
    public Boolean doInBackground(URL... params) {
        URL url = params[0];
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }

                Log.d(TAG, result.toString());
                List<Book> books = JsonUtils.parseBooksJson(result.toString());
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    final DatabaseBook databaseRecipe = new DatabaseBook(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getBody(),
                            book.getThumb(),
                            book.getPhoto(),
                            book.getAspect_ratio(),
                            book.getPublished_date()
                    );
                    mXYZDatabase.xyzDao().insertBook(databaseRecipe);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean data) {
        mRemoteListener.postExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mRemoteListener.postExecute();
    }
}
