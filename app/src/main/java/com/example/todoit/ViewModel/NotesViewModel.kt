package com.example.todoit.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.todoit.Database.NotesDatabase
import com.example.todoit.Model.Notes
import com.example.todoit.Repository.NotesRepository

class NotesViewModel(application: Application): AndroidViewModel(application) {

    val repository : NotesRepository

    init {
        val dao = NotesDatabase.getDatabaseInstance(application).myNotesDao()
        repository = NotesRepository(dao)
    }
    fun addNotes(notes : Notes){
        repository.insertNotes(notes)
    }

    fun getNotes():LiveData<List<Notes>> = repository.getAllNotes()

    fun getLowNotes(): LiveData<List<Notes>>{
        return repository.getLowNotes()
    }


    fun getMediumNotes(): LiveData<List<Notes>> {
        return repository.getMediumNotes()
    }


    fun getHighNotes(): LiveData<List<Notes>> {
        return repository.getHighNotes()
    }


    fun deleteNotes(id : Int){
        repository.deleteNotes(id)
    }

    fun updateNotes(notes : Notes){
        repository.updateNotes(notes)
    }

}