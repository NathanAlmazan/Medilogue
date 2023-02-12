package com.automata.medilogue

import android.util.Log
import androidx.lifecycle.*
import com.automata.medilogue.data.*
import com.automata.medilogue.repos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

class ChatViewModel(
    private val illnessesRepo: IllnessesRepo,
    private val medicineRepo: MedicineRepo
): ViewModel() {
    private val machineState: MutableLiveData<State> by lazy {
        MutableLiveData<State>().also {
            it.value = State.START_CONVERSATION
        }
    }

    val messageList: MutableLiveData<List<Message>> by lazy {
        MutableLiveData<List<Message>>().also {
            val messages = ArrayList<Message>()
            messages.add(Message("What symptoms do you experience?", ChatTypes.CHAT_BOT))
            it.postValue(messages)
        }
    }

    private val illnessQuestions: MutableLiveData<List<SymptomQuestion>> by lazy {
        MutableLiveData<List<SymptomQuestion>>()
    }

    private val medicineQuestions: MutableLiveData<List<MedQuestions>> by lazy {
        MutableLiveData<List<MedQuestions>>()
    }

    private val selectedIllness: MutableLiveData<Illnesses> by lazy {
        MutableLiveData<Illnesses>()
    }

    private val tempIllness: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun handleMessageSent(message: String) {
        machineState.value?.let { it ->
            when (it) {
                State.START_CONVERSATION -> handleStartConversation(message)
                State.CONSULTATION -> handleConsultation(message)
                State.CONSULTATION_CONCLUSION -> handleMedicineRecommendation(message)
                State.MEDICINE_VERIFICATION -> handleMedicineVerification(message)
                State.MEDICINE_RECOMMENDATION -> handleResetConversation()
            }
        }
    }

    private fun handleConsultation(message: String) {
        illnessQuestions.value?.let {
            val questionList = ArrayList<SymptomQuestion>(it)

            val cleanedMessage = message.lowercase().replace("[^a-zA-Z0-9]".toRegex(), "")

            Log.d("message", cleanedMessage)

            if (checkResponse(cleanedMessage, true))
                handleConsultationConclusion(questionList.removeAt(0).illnessId, Message(message, ChatTypes.USER_RESPONSE))
            else if (checkResponse(cleanedMessage, false)) {
                val question = questionList.removeAt(0)

                if (questionList.isEmpty()) {
                    handleConsultationConclusion(tempIllness.value!!, Message(message, ChatTypes.USER_RESPONSE))
                    return
                }

                while (questionList[0].message.content == question.message.content) {
                    questionList.removeAt(0)

                    if (questionList.isEmpty()) {
                        handleConsultationConclusion(tempIllness.value!!, Message(message, ChatTypes.USER_RESPONSE))
                        return
                    }
                }

                sendMessage(questionList[0].message, Message(message, ChatTypes.USER_RESPONSE))
                illnessQuestions.postValue(questionList)
            } else
                sendMessage(Message("Sorry I can't catch up to that. Please try again.", ChatTypes.CHAT_BOT), Message(message, ChatTypes.USER_RESPONSE))
        }
    }

    private fun checkResponse(response: String, positive: Boolean): Boolean {
        if (positive) {
            for (word in positiveResponses) {
                if (response.contains(word) || word.contains(response)) return true
            }
            return false
        } else {
            for (word in negativeResponses) {
                if (response.contains(word) || word.contains(response)) return true
            }
            return false
        }
    }

    private fun handleConsultationConclusion(illnessId: Int, message: Message? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            illnessesRepo.findIllnessById(illnessId).collect { illness ->
                sendMessage(Message(
                    "You may have ${illness.name}. ${illness.description} \n\nPatients also asks: ", ChatTypes.CHAT_BOT
                ), message, listOf("What causes ${illness.name}?", "What medicine can I take to relieve symptoms of ${illness.name}?")
                )

                selectedIllness.postValue(illness)
                machineState.postValue(State.CONSULTATION_CONCLUSION)
            }
        }
    }

    private fun handleMedicineRecommendation(message: String) {
        val illness = selectedIllness.value!!
        if (message.contains("What causes")) {
            sendMessage(Message(illness.causes, ChatTypes.CHAT_BOT), Message(message, ChatTypes.USER_RESPONSE))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                medicineRepo.findMedicineConditionsByMed(illness.illMedId).collect { conditions ->

                    if (conditions.isEmpty()) {
                        handleMedicineConclusion(Message(message, ChatTypes.USER_RESPONSE))
                        return@collect
                    }

                    val questionsList = mutableListOf<MedQuestions>()
                    for (condition in conditions)
                        questionsList.add(
                            MedQuestions(Message(condition.question, ChatTypes.CHAT_BOT), condition.answer)
                        )

                    sendMessage(questionsList[0].message, Message(message, ChatTypes.USER_RESPONSE))
                    medicineQuestions.postValue(questionsList)
                    machineState.postValue(State.MEDICINE_VERIFICATION)
                }
            }
        }
    }

    private fun handleMedicineVerification(message: String) {
        medicineQuestions.value?.let {
            val questionList = ArrayList<MedQuestions>(it)
            val currentQuestion = questionList.removeAt(0)

            if (message.lowercase() != "yes" && message.lowercase() != "no") {
                sendMessage(Message("Pardon? Please answer yes or no only.", ChatTypes.CHAT_BOT), Message(message, ChatTypes.USER_RESPONSE))
                return
            }

            if (message.lowercase() == "yes" && currentQuestion.answer == "True" ||
                    message.lowercase() == "no" && currentQuestion.answer == "False") {
                if (questionList.isNotEmpty()) {
                    sendMessage(questionList[0].message, Message(message, ChatTypes.USER_RESPONSE))
                    medicineQuestions.postValue(questionList)
                }
                else handleMedicineConclusion(Message(message, ChatTypes.USER_RESPONSE))
            }
            else {
                val response = Message("Sorry we don't have recommended medicine for your condition. Please consider consulting a doctor.", ChatTypes.CHAT_BOT)
                sendMessage(response, Message(message, ChatTypes.USER_RESPONSE), listOf("Do another consultation?"))
                machineState.postValue(State.MEDICINE_RECOMMENDATION)
            }
        }
    }

    private fun handleMedicineConclusion(message: Message) {
        val illness = selectedIllness.value!!
        viewModelScope.launch(Dispatchers.IO) {
            medicineRepo.findMedicineById(illness.illMedId).collect { medicine ->
                val response = "You can take ${medicine.name} to relieve symptoms of ${illness.name}. ${medicine.description}\n\nReminder: \n${medicine.reminders}"
                sendMessage(Message(response, ChatTypes.CHAT_BOT), message, listOf("Do another consultation?"))
                machineState.postValue(State.MEDICINE_RECOMMENDATION)
            }
        }
    }

    private fun handleResetConversation() {
        val messages = ArrayList<Message>()
        messages.add(Message("What symptoms do you experience?", ChatTypes.CHAT_BOT))
        messageList.postValue(messages)
        machineState.postValue(State.START_CONVERSATION)
    }



    private fun handleStartConversation(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            illnessesRepo.findAllSymptoms().collect { symptoms ->
                // clean and collect query words
                val words = message.split(" ")
                val cleanedWords = mutableListOf<String>()

                for (word in words)
                    cleanedWords.add(word.replace("[^a-zA-Z0-9]".toRegex(), "").lowercase())

                Log.d("Algo", cleanedWords.toString())

                // collect results
                val illnessIds = mutableListOf<Int>()
                for (word in cleanedWords)
                    illnessIds.addAll(symptoms.filter { s -> s.name.contains(word) }.map { s -> s.sympIllId })

                if (illnessIds.isEmpty()) {
                   if (cleanedWords.size > 2) {
                       sendMessage(
                           Message("Sorry, I can't recognize your symptom yet. Please consider consulting a doctor.", ChatTypes.CHAT_BOT), Message(message, ChatTypes.USER_RESPONSE)
                       )
                   }
                    else {
                       sendMessage(
                           Message("Sorry, I can't recognize your symptom. Can you please please elaborate?", ChatTypes.CHAT_BOT), Message(message, ChatTypes.USER_RESPONSE)
                       )
                   }
                    return@collect
                }

                // collect symptoms
                val uniqueIds = illnessIds.distinct()

                Log.d("Algo", uniqueIds.toString())

                val illnessMap = HashMap<Int, List<String>>()
                for (id in uniqueIds)
                    illnessMap[id] = symptoms.filter { s -> s.sympIllId == id }.map { s -> s.name }

                // accuracy poll
                val illnessPoll = HashMap<Int, Double>()
                for (id in uniqueIds) {
                    val symptomsList = illnessMap[id]!!
                    illnessPoll[id] = computeCosineDistance(
                        cleanedWords.joinToString(" "), symptomsList.joinToString(" "))
                }

                Log.d("Algo", illnessPoll.toString())

                val sortedIds = uniqueIds.sortedWith(compareBy { illnessPoll[it] }).asReversed()

                Log.d("Algo", sortedIds.toString())

                tempIllness.postValue(sortedIds[0])

                val questionList = mutableListOf<SymptomQuestion>()
                val usedSymptoms = mutableListOf<String>()
                for (id in sortedIds) {
                    val sympList = illnessMap[id]!!.filter { s -> !cleanedWords.contains(s) && !usedSymptoms.contains(s) }

                    if (sympList.isEmpty()) break

                    val m = Message("Do you experience ${symptomsListGenerator(sympList)}?", ChatTypes.CHAT_BOT)

                    questionList.add(SymptomQuestion(m, id))
                    usedSymptoms.addAll(sympList)
                }

                Log.d("Algo", questionList.toString())
                sendMessage(questionList[0].message, Message(message, ChatTypes.USER_RESPONSE))
                illnessQuestions.postValue(questionList)
                machineState.postValue(State.CONSULTATION)
            }
        }
    }

    private fun sendMessage(bot: Message, user: Message?, options: List<String>? = null) {
        messageList.value?.let {
            val messages = ArrayList<Message>(it)
            if (user != null) messages.add(user)
            messages.add(bot)

            if (options != null && options.isNotEmpty()) {
                Log.d("Algo", options.toString())
                for (option in options) messages.add(Message(option, ChatTypes.OPTIONS))
            }

            messageList.postValue(messages)
        }
    }

    private fun symptomsListGenerator(symptoms: List<String>): String {
        val list = ArrayList<String>(symptoms)
        val lastSymptom = list.removeLast()
        return list.joinToString(", ") + ", or " + lastSymptom
    }

    private fun computeCosineDistance(query: String, result: String): Double {
        val wordsA = query.split(" ")
        val wordsB = result.split(" ")

        val setA = java.util.HashMap<String, Int>()
        val setB = java.util.HashMap<String, Int>()

        wordsA.forEach { word ->
            // if exist update count else add word to set A
            if (setA.containsKey(word)) setA.computeIfPresent(word) { _, v -> v + 1 }
            else setA[word] = 1

            // add word to second set
            if (!setB.containsKey(word)) setB[word] = 0
        }

        wordsB.forEach { word ->
            // if exist update count else add word to set A
            if (setB.containsKey(word)) setB.computeIfPresent(word) { _, v -> v + 1 }
            else setB[word] = 1

            // add word to first set
            if (!setA.containsKey(word)) setA[word] = 0
        }

        // Get L2 norm
        var setANorm = 0.0
        var setBNorm = 0.0

        // Summing up squares of each frequency
        for ((_, value) in setA) setANorm += value.toDouble().pow(2.0)
        for ((_, value) in setB) setBNorm += value.toDouble().pow(2.0)

        // taking a square root of summation
        setANorm = sqrt(setANorm)
        setBNorm = sqrt(setBNorm)

        // compute cosine distance
        var cosineDistance = 0.0
        for ((key, countA) in setA) {
            // Get count in B
            val countB = setB[key]
            if (countB != null && countA > 0 && countB > 0) {
                cosineDistance += (countA / setANorm) * (countB / setBNorm)
            }
        }

        return cosineDistance * 100
    }

    companion object {
        val positiveResponses = listOf("yes", "i agree", "some", "little", "exactly")
        val negativeResponses = listOf("no", "i dont know", "maybe", "im not sure", "not")
    }
}

class ChatViewModelFactory(private val database: MedDatabase): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            val illnessesRepo = IllnessesRepo(database.getIllnessesDao())
            val medicineRepo = MedicineRepo(database.getMedicinesDao())
            return ChatViewModel(illnessesRepo, medicineRepo) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}