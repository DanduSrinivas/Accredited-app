package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GapItem::class, EvidenceItem::class, NarrativeSection::class], version = 1, exportSchema = false)
abstract class AccreditationDatabase : RoomDatabase() {
    abstract fun dao(): AccreditationDao

    companion object {
        @Volatile
        private var INSTANCE: AccreditationDatabase? = null

        fun getDatabase(context: Context): AccreditationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccreditationDatabase::class.java,
                    "accreditation_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
