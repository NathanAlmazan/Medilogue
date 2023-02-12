package com.automata.medilogue.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MedicineDao {

    @Query("SELECT * FROM med_conditions WHERE cond_med_id = :medId")
    fun findMedicineConditionsByMed(medId: Int): LiveData<List<MedicineConditions>>

    @Query("SELECT * FROM medicines WHERE med_id = :id")
    fun findMedicineById(id: Int): LiveData<Medicines>

}