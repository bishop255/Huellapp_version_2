package com.example.huellapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.huellapp.repository.PerroRepository
import com.example.huellapp.viewmodel.PerroViewModel

class PerroViewModelFactory(private val repository: PerroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
