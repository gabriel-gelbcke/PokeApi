package com.example.pokeapi.network

import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true) // Para Moshi, caso esteja usando
data class PokemonApiResponse(
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites
)

data class Sprites(
    val front_default: String?,
    val other: OtherSprites
)

data class OtherSprites(
    val dream_world: DreamWorldSprites
)

data class DreamWorldSprites(
    val front_default: String?
)


