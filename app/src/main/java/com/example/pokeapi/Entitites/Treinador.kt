package com.example.pokeapi.Entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "treinador_table")
data class Treinador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)
