package com.example.pokeapi.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import com.example.pokeapi.network.PokemonApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    // Configuração do Retrofit com o GsonConverterFactory
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // Adicionando o Gson Converter
        .build()

    val pokeApiService: PokeApiService = retrofit.create(PokeApiService::class.java)
}
