package com.example.pokeapi.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface PokeApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemonByName(
        @Path("name") name: String
    ): Response<PokemonApiResponse> // Adicionando Response
}
