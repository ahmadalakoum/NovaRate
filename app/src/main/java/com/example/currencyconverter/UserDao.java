package com.example.currencyconverter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM User WHERE username = :username")
    User getUserByUsername(String username);

}
