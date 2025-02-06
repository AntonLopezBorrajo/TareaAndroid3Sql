package com.montecastelo.appantonlopez.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity()
data class Ejercicios(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String
)
@Dao
interface EjerciciosDao {
    // Obtener todos los ejercicios (debe ejecutarse en una corrutina)
    @Query("SELECT * FROM Ejercicios")
    suspend fun selectall(): List<Ejercicios>

    // Insertar un nuevo ejercicio
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExercise(ejercicio: Ejercicios)

    // Eliminar un ejercicio por nombre
    @Query("DELETE FROM Ejercicios WHERE nombre = :nombredelete")
    suspend fun borrarejercicio(nombredelete: String)

    // Actualizar un ejercicio
    @Query("UPDATE Ejercicios SET nombre = :nuevoNombre WHERE nombre = :nombreAntiguo")
    suspend fun actualizarNombre(nombreAntiguo: String, nuevoNombre: String)
}

@Database(version = 1, entities = [Ejercicios::class])
abstract class EjerciciosDB : RoomDatabase(){
    abstract fun EjerciciosBase() : EjerciciosDao
}