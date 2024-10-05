package com.example.todoit.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoit.Dao.NotesDao
import com.example.todoit.Model.Notes

@Database(entities = [Notes::class], version = 2, exportSchema = false) // Incremented version
abstract class NotesDatabase : RoomDatabase() {
    abstract fun myNotesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabaseInstance(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val roomDatabaseInstance = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "Notes"
                )
                    .addMigrations(MIGRATION_1_2) // Add migration here
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = roomDatabaseInstance
                roomDatabaseInstance
            }
        }

        // Define your migration
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add your new column(s) here. Example for adding a 'time' column.
                database.execSQL("ALTER TABLE Notes ADD COLUMN time TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
