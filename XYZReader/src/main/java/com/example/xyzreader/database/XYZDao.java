package com.example.xyzreader.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface XYZDao {

    @Query("SELECT * FROM DatabaseBook")
    LiveData<List<DatabaseBook>> getBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(DatabaseBook recipe);
}