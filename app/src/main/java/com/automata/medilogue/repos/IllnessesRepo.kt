package com.automata.medilogue.repos

import androidx.lifecycle.asFlow
import com.automata.medilogue.data.Illnesses
import com.automata.medilogue.data.IllnessesDao
import com.automata.medilogue.data.Symptoms
import kotlinx.coroutines.flow.Flow

class IllnessesRepo(private val illnessesDao: IllnessesDao) {

    fun findAllSymptoms(): Flow<List<Symptoms>>
        = illnessesDao.findAllSymptoms().asFlow()

    fun findIllnessById(id: Int): Flow<Illnesses>
        = illnessesDao.findIllnessById(id).asFlow()
}