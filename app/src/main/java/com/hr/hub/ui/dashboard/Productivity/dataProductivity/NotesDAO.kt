package com.hr.hub.ui.dashboard.Productivity.dataProductivity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface NotesDAO {

    @Query("SELECT*FROM `My Notes` ORDER BY id DESC")
    suspend fun getAllNotes():List<NotesEntity>

    @Insert
    suspend fun insertNotes(note:NotesEntity)

    @Delete
    suspend fun deleteNote(note: NotesEntity)



}