package com.krealif.beritaku.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.krealif.beritaku.model.News;

import java.util.List;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(News news);

    @Delete
    void delete(News news);

    @Query("SELECT * FROM news")
    LiveData<List<News>> getAllNews();
}
