package com.example.noteapplication.remote.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.noteapplication.models.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("UPDATE note_table Set title=:title,note=:note WHERE id=:id")
    suspend fun update(id: Int?, title: String?, note: String?)

    @Query("Select * from note_table order by id ASC")
    fun getAllNoteList(): LiveData<List<Note>>
}