package com.automata.medilogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.automata.medilogue.data.MedDatabase
import com.automata.medilogue.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // view
    private lateinit var binding: ActivityMainBinding

    // view model
    private lateinit var chatViewModel: ChatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // room database
        val database = MedDatabase.getDatabase(application)

        // initialize view model
        chatViewModel = ViewModelProvider(this, ChatViewModelFactory(database))[ChatViewModel::class.java]
    }
}