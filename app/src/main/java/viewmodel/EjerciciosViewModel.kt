package com.montecastelo.appantonlopez.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.montecastelo.appantonlopez.data.Ejercicios
import com.montecastelo.appantonlopez.data.EjerciciosDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class EjerciciosViewModel @Inject constructor(
    private val dao: EjerciciosDao
) : ViewModel() {

    private val _ejercicios = MutableStateFlow<List<Ejercicios>>(emptyList())
    val ejercicios: StateFlow<List<Ejercicios>> = _ejercicios

    init {
        cargarEjercicios()
    }

    private fun cargarEjercicios() {
        viewModelScope.launch(Dispatchers.IO) {
            _ejercicios.value = dao.selectall()
        }
    }

    fun agregarEjercicio(nombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val nuevoEjercicio = Ejercicios(nombre = nombre)
            dao.addExercise(nuevoEjercicio)
            cargarEjercicios()
        }
    }

    fun eliminarEjercicio(nombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.borrarejercicio(nombre)
            cargarEjercicios()
        }
    }

    fun actualizarEjercicio(nombreAntiguo: String, nuevoNombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.actualizarNombre(nombreAntiguo, nuevoNombre)
            cargarEjercicios()
        }
    }
}



