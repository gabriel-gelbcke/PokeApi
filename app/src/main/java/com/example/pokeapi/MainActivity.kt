package com.example.pokeapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokeapi.network.PokemonApiResponse
import com.example.pokeapi.ui.theme.MyTheme
import com.example.pokeapi.ui.theme.PokemonDetails
import com.example.pokeapi.viewmodel.PokemonViewModel

class MainActivity : ComponentActivity() {
    private val pokemonViewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura a cor da barra de status
        window.statusBarColor = getColor(R.color.my_status_bar_color) // Cor da barra de status
        setContent {
            MyApp {
                Search(viewModel = pokemonViewModel)
            }
        }
        pokemonViewModel.getPokemonByName("pikachu")
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black // Use a cor de fundo do tema
        ) {
            content()
        }
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
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de texto para pesquisa
        TextField(
            value = busca.value,
            onValueChange = { busca.value = it.trim() }, // Remover espaços no início e fim
            label = { Text("Nome do Pokémon") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Botão para pesquisar Pokémon
        Button(
            onClick = {
                errorMessage.value = "" // Limpa mensagens anteriores
                if (busca.value.isNotBlank()) {
                    viewModel.getPokemonByName(busca.value.lowercase()) // Converte para minúsculas
                } else {
                    errorMessage.value = "Digite um nome válido!"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan), // Define a cor do botão
            shape = RoundedCornerShape(8.dp), // Bordas arredondadas
            elevation = ButtonDefaults.elevatedButtonElevation(), // Elevação do botão
            modifier = Modifier
                .fillMaxWidth(0.8f) // Define a largura do botão
                .height(60.dp) // Define a altura do botão
                .padding(horizontal = 16.dp) // Adiciona preenchimento horizontal para centralizar
        ) {
            Text(
                text = "Pesquisar Pokémon",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
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

        Spacer(modifier = Modifier.height(26.dp))

        // Exibe mensagem de Pokémon não encontrado
        if (pokemonState == null && errorMessage.value.isEmpty()) {
            Text(
                text = "Pokémon não encontrado.",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
