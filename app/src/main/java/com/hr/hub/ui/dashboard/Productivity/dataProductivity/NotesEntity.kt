package com.hr.hub.ui.dashboard.Productivity.dataProductivity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "My Notes")

data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "Tittle")
    internal val tittle: String?,

    @ColumnInfo(name = "Content")
    internal val content: String?
)

