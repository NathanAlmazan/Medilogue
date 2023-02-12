package com.automata.medilogue.repos

import com.automata.medilogue.data.Message

data class MedQuestions (
    val message: Message,
    val answer: String
)