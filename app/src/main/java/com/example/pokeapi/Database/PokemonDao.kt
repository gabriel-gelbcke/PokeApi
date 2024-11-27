package com.example.pokeapi.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.pokeapi.Entitites.Pokemon

@Dao
interface PokemonDao {

    @Insert
    suspend fun insert(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon_table")
    suspend fun getAll(): List<Pokemon>

    @Delete
    suspend fun delete(pokemon: Pokemon)
}
