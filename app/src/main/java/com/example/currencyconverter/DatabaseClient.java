package com.example.currencyconverter;

import android.content.Context;

import androidx.room.Room;

// Singleton helper to get a single instance of AppDatabase
public class DatabaseClient {

    private static AppDatabase db;

    public static AppDatabase getDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "user-database" // database file name
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() // resets DB if version changes
                    .build();
        }
        return db;
    }
}
