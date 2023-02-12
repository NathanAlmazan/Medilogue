package com.automata.medilogue.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "illnesses")
data class Illnesses (
    @PrimaryKey @ColumnInfo(name = "ill_id") val illId: Int,
    @ColumnInfo(name = "ill_name") val name: String,
    @ColumnInfo(name = "ill_description") val description: String,
    @ColumnInfo(name = "ill_causes") val causes: String,
    @ColumnInfo(name = "ill_source") val source: String,
    @ColumnInfo(name = "ill_medicine_id") val illMedId: Int
)

@Entity(tableName = "symptoms")
data class Symptoms (
    @PrimaryKey @ColumnInfo(name = "symp_id") val sympId: Int,
    @ColumnInfo(name = "symp_name") val name: String,
    @ColumnInfo(name = "symp_ill_id") val sympIllId: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int
)

@Entity(tableName = "categories")
data class Categories (
    @PrimaryKey @ColumnInfo(name = "categ_id") val categId: Int,
    @ColumnInfo(name = "categ_name") val categName: String
)