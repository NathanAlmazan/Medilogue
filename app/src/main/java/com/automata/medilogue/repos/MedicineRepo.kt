package com.automata.medilogue.repos

import androidx.lifecycle.asFlow
import com.automata.medilogue.data.MedicineConditions
import com.automata.medilogue.data.MedicineDao
import com.automata.medilogue.data.Medicines
import kotlinx.coroutines.flow.Flow

class MedicineRepo(private val medicineDao: MedicineDao) {

    fun findMedicineConditionsByMed(medId: Int): Flow<List<MedicineConditions>>
        = medicineDao.findMedicineConditionsByMed(medId).asFlow()

    fun findMedicineById(id: Int): Flow<Medicines>
        = medicineDao.findMedicineById(id).asFlow()

}