package com.automata.medilogue.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
    Medicines::class,
    MedicineConditions::class,
    Illnesses::class,
    Symptoms::class,
    Categories::class
], version = 1, exportSchema = false)
abstract class MedDatabase: RoomDatabase() {
    abstract fun getMedicinesDao(): MedicineDao
    abstract fun getIllnessesDao(): IllnessesDao

    companion object {
        @Volatile
        private var INSTANCE: MedDatabase? = null

        private const val DB_NAME = "medilogue.db"

        fun getDatabase(context: Context): MedDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedDatabase::class.java,
                    DB_NAME
                )
                    .createFromAsset("medilogue.db")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}