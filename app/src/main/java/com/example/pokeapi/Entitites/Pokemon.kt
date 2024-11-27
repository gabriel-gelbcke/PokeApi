package com.example.pokeapi.Entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)
