package com.example.noteapplication.repository

import androidx.lifecycle.LiveData
import com.example.noteapplication.remote.database.NoteDao
import com.example.noteapplication.models.Note

class NoteRepository(private var noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNoteList()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note.id, note.title, note.note)
    }
}