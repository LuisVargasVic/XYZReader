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

    @Query("SELECT * FROM DatabaseBook WHERE id = :id")
    LiveData<DatabaseBook> getBook(int id);

    @Query("SELECT COUNT(*) FROM DatabaseBook")
    LiveData<Integer> getNumberBook();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(DatabaseBook recipe);
}