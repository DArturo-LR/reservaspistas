package com.example.reservaspistas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.reservaspistas.data.AppDatabase
import com.example.reservaspistas.repository.ReservaRepository
import com.example.reservaspistas.ui.ReservaScreen
import com.example.reservaspistas.viewmodel.ReservaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.Companion.getDatabase(applicationContext)
        val repository = ReservaRepository(database.reservaDao())
        val viewModel = ReservaViewModel(repository)

        setContent {
            ReservaScreen(viewModel)
        }
    }
}