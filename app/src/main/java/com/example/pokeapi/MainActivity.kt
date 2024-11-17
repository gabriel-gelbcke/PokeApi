package com.example.pokeapi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokeapi.network.PokemonApiResponse
import com.example.pokeapi.ui.theme.PokeApiTheme
import com.example.pokeapi.viewmodel.PokemonViewModel
import coil.compose.AsyncImage
import com.example.pokeapi.ui.theme.PokemonDetails

class MainActivity : ComponentActivity() {
    private val pokemonViewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeApiTheme {
                Search(viewModel = pokemonViewModel)
            }
        }
        pokemonViewModel.getPokemonByName("pikachu")
    }
}

@Composable
fun Search(viewModel: PokemonViewModel) {
    val busca = remember { mutableStateOf("") }
    val pokemonState by viewModel.pokemon.observeAsState()
    val errorMessage = remember { mutableStateOf("") } // Estado para exibir erros

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de texto para pesquisa
        TextField(
            value = busca.value,
            onValueChange = { busca.value = it.trim() }, // Remover espaços no início e fim
            label = { Text("Nome do Pokémon") },
            modifier = Modifier.fillMaxWidth()
        )

        // Botão para pesquisar Pokémon
        Button(
            onClick = {
                errorMessage.value = "" // Limpa mensagens anteriores
                if (busca.value.isNotBlank()) {
                    viewModel.getPokemonByName(busca.value)
                } else {
                    errorMessage.value = "Digite um nome válido!"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pesquisar Pokémon")
        }

        // Exibe mensagem de erro, se houver
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Exibe os detalhes do Pokémon em um Card
        pokemonState?.let { pokemon ->
            Spacer(modifier = Modifier.height(16.dp))
            PokemonDetails(pokemon = pokemon)
        }

        // Exibe mensagem de Pokémon não encontrado
        if (pokemonState == null && errorMessage.value.isEmpty()) {
            Text(
                text = "Pokémon não encontrado.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}