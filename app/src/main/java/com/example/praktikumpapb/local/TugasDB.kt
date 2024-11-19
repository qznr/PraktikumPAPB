package com.example.praktikumpapb.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Tugas::class], version = 1, exportSchema = false)
@TypeConverters(UriConverter::class)
abstract class TugasDB : RoomDatabase() {
    abstract fun tugasDao(): TugasDAO

    companion object {
        @Volatile
        private var INSTANCE: TugasDB? = null

        @JvmStatic
        fun getDatabase(context: Context): TugasDB {
            if (INSTANCE == null) {
                synchronized(TugasDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TugasDB::class.java,
                        "tugas_database"
                    ).build()
                }
            }
            return INSTANCE as TugasDB
        }
    }
}