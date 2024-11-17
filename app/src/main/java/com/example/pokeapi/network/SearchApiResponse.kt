package com.example.pokeapi.network

import com.squareup.moshi.JsonClass

data class PokemonApiResponse(
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val abilities: List<Ability>,
    val stats: List<Stat>,
    val sprites: Sprites
)

data class Type(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

data class Ability(
    val ability: AbilityInfo
)

data class AbilityInfo(
    val name: String
)

data class Stat(
    val stat: StatInfo,
    val base_stat: Int
)

data class StatInfo(
    val name: String
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

//data class PokemonApiResponse(
//    val name: String,
//    val height: Int,
//    val weight: Int,
//    val sprites: Sprites
//)
//
//data class Sprites(
//    val front_default: String?,
//    val other: OtherSprites
//)
//
//data class OtherSprites(
//    val dream_world: DreamWorldSprites
//)
//
//data class DreamWorldSprites(
//    val front_default: String?
//)


