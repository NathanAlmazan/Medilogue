package com.automata.medilogue.data;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicines (
        @PrimaryKey @ColumnInfo(name = "med_id") val medId: Int,
        @ColumnInfo(name = "med_name") val name: String,
        @ColumnInfo(name = "med_description") val description: String,
        @ColumnInfo(name = "med_reminders") val reminders: String,
        @ColumnInfo(name = "med_source") val source: String
)

@Entity(tableName = "med_conditions")
data class MedicineConditions (
        @PrimaryKey @ColumnInfo(name = "cond_id") val condId: Int,
        @ColumnInfo(name = "cond_med_id") val condMedId: Int,
        @ColumnInfo(name = "cond_question") val question: String,
        @ColumnInfo(name = "cond_answer") val answer: String
)
