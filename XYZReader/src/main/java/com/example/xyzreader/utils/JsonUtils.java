package com.example.xyzreader.utils;

import com.example.xyzreader.domain.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_BODY = "body";
    private static final String KEY_THUMB = "thumb";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_ASPECT_RATIO = "aspect_ratio";
    private static final String KEY_PUBLISHED_DATE = "published_date";

    public static List<Book> parseBooksJson(String json) {
        List<Book> books = new ArrayList<>();
        try {
            JSONArray objects = new JSONArray(json);
            for (int i = 0; i < objects.length(); i++) {
                JSONObject recipe = objects.getJSONObject(i);
                int id = recipe.getInt(KEY_ID);
                String title = recipe.getString(KEY_TITLE);
                String author = recipe.getString(KEY_AUTHOR);
                String body = recipe.getString(KEY_BODY);
                String thumb = recipe.getString(KEY_THUMB);
                String photo = recipe.getString(KEY_PHOTO);
                Float aspectRatio = Float.valueOf(recipe.getString(KEY_ASPECT_RATIO));
                String publishedDate = recipe.getString(KEY_PUBLISHED_DATE);
                books.add(new Book(id, title, author, body, thumb, photo, aspectRatio, publishedDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

}
