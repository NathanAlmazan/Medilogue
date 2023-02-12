package com.automata.medilogue.repos

import com.automata.medilogue.data.Message

data class SymptomQuestion(
    val message: Message,
    val illnessId: Int,
    val next: List<SymptomQuestion>? = null,
)
