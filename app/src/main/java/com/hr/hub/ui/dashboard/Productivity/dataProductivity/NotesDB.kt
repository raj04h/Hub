package com.hr.hub.ui.dashboard.Productivity.dataProductivity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [NotesEntity::class], version = 1)
abstract class NotesDB : RoomDatabase() {

    abstract fun notesdao(): NotesDAO

    companion object {
        @Volatile
        private var INSTANCE: NotesDB? = null

        // Define migration object
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add SQL statements to handle the schema change
                database.execSQL("ALTER TABLE `My Notes` ADD COLUMN new_column_name INTEGER DEFAULT 0 NOT NULL")
            }
        }

        fun getData(context: Context): NotesDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDB::class.java,
                    "Notes_Database"
                )
                    .addMigrations(MIGRATION_1_2) // Add your migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
