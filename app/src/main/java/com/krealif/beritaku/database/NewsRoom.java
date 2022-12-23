package com.krealif.beritaku.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.krealif.beritaku.model.News;

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class NewsRoom extends RoomDatabase {
    public abstract NewsDao newsDao();
    public static volatile NewsRoom INSTANCE;

    public static NewsRoom getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (NewsRoom.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        NewsRoom.class, "note_database").build();
            }
        }
        return INSTANCE;
    }
}
