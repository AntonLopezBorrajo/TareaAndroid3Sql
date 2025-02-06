package com.montecastelo.appantonlopez.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.montecastelo.appantonlopez.data.EjerciciosDao

class EjerciciosViewModelFactory(private val dao: EjerciciosDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EjerciciosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EjerciciosViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

