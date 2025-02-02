package com.example.pokeapi.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.pokeapi.Entitites.Treinador;

@Dao
interface TreinadorDao {
    @Insert
    suspend fun insert(treinador:Treinador)

    @Update
    suspend fun update(treinador: Treinador)

    @Query("SELECT * FROM treinador_table")
    suspend fun getAll(): List<Treinador>

    @Delete
    suspend fun delete(treinador: Treinador)
}