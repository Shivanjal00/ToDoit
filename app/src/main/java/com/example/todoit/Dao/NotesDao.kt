package com.example.todoit.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoit.Model.Notes

@Dao
interface NotesDao {


    @Query("SELECT * FROM notes")
    fun getNotes():LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE priority = 3")
    fun getHighNotes():LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE priority = 2")
    fun getMediumNotes():LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE priority = 1")
    fun getLowNotes():LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: Notes)

    @Query("DELETE FROM Notes WHERE id =:id")
    fun deleteNotes(id: Int)

    @Update
    fun updateNotes(notes: Notes)


}