package com.siriwardana.whatsmyhandicap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUser();

    @Query("SELECT COUNT(*) FROM user WHERE user.email = :email")
    int getUserCountByEmail(String email);

    @Query("SELECT * FROM user WHERE user.email =:email")
    User getUserByEmail(String email);

    @Insert
    void insert(User... users);

    @Delete
    void delete(User user);

}
