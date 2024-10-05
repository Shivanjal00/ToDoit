package com.example.todoit.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoit.Model.Notes

@Dao
interface NotesDao {

    @Query("SELECT * FROM Notes") // Changed to "Notes"
    fun getNotes(): LiveData<List<Notes>>

    @Query("SELECT * FROM Notes WHERE priority = 3") // Changed to "Notes"
    fun getHighNotes(): LiveData<List<Notes>>

    @Query("SELECT * FROM Notes WHERE priority = 2") // Changed to "Notes"
    fun getMediumNotes(): LiveData<List<Notes>>

    @Query("SELECT * FROM Notes WHERE priority = 1") // Changed to "Notes"
    fun getLowNotes(): LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: Notes)

    @Query("DELETE FROM Notes WHERE id = :id") // This line is correct.
    fun deleteNotes(id: Int)

    @Update
    fun updateNotes(notes: Notes)
}
