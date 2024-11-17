package com.example.pokeapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.network.PokemonApiResponse
import com.example.pokeapi.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class PokemonViewModel : ViewModel() {

    private val _pokemon = MutableLiveData<PokemonApiResponse?>()
    val pokemon: LiveData<PokemonApiResponse?> = _pokemon

    fun getPokemonByName(name: String) {
        viewModelScope.launch {
            val response: Response<PokemonApiResponse> = RetrofitInstance.pokeApiService.getPokemonByName(name)
            if (response.isSuccessful) {
                _pokemon.value = response.body()
            } else {
                Log.e("PokemonViewModel", "Erro ao carregar o Pokémon: ${response.code()}")
                _pokemon.value = null
                Log.d("PokemonDetails", "Resposta do Pokémon: ${response.body()}")

            }
        }
    }
}
