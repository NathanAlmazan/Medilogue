package com.automata.medilogue.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface IllnessesDao {

    @Query("SELECT * FROM symptoms")
    fun findAllSymptoms(): LiveData<List<Symptoms>>

    @Query("SELECT * FROM illnesses WHERE ill_id = :id")
    fun findIllnessById(id: Int): LiveData<Illnesses>

}