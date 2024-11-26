package com.example.pokeapi

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokeapi.network.PokemonApiResponse
import com.example.pokeapi.ui.theme.MyTheme
import com.example.pokeapi.ui.theme.PokemonDetails
import com.example.pokeapi.viewmodel.PokemonViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


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
fun MainScreen(navController: NavController) {
        // Este Row ficará fixado no topo da tela
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Botão de navegação para a tela de CRUDs
            IconButton(
                onClick = { navController.navigate("mainCrud") }, // Redireciona para a tela de CRUDs
                modifier = Modifier
                    .size(width = 90.dp, height = 50.dp) // Define o tamanho do botão
                    .padding(end = 8.dp) // Espaço entre o ícone e o texto
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Alinha verticalmente o ícone e o texto
                    horizontalArrangement = Arrangement.Start, // Alinha no início da linha
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward, // Usando a seta para frente para indicar navegação
                        contentDescription = "Ir para CRUD",
                        tint = Color.White // Cor do ícone
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
                    Text(
                        "Ir para CRUD",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterVertically) // Garante que o texto se alinha ao centro verticalmente
                    )
                }
            }
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

    val navController = rememberNavController()

    // Configuração do NavHost para navegação entre telas
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("mainCrud") {
            MainCrud(navController)
        }
    }
}

@Composable
fun Search(viewModel: PokemonViewModel) {
    val busca = remember { mutableStateOf("") }
    val pokemonState by viewModel.pokemon.observeAsState()
    val errorMessage = remember { mutableStateOf("") }
    val context = LocalContext.current
    val scrollState = rememberScrollState() // Estado de rolagem

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Adiciona rolagem
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Seu conteúdo aqui
        TextField(
            value = busca.value,
            onValueChange = { busca.value = it.trim() },
            label = { Text("Nome do Pokémon") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                errorMessage.value = ""
                if (busca.value.isNotBlank()) {
                    viewModel.getPokemonByName(busca.value.lowercase())
                } else {
                    errorMessage.value = "Digite um nome válido!"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Pesquisar Pokémon",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }


        pokemonState?.let { pokemon ->
            PokemonDetails(pokemon = pokemon)
        }

        pokemonState?.let { pokemon ->
            Button(
                onClick = {
                    val pokemonName = pokemon.name.capitalize()
                    sharePokemonName(context, pokemonName)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
            ) {
                Text(
                    text = "Compartilhar Pokémon",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(0.dp))

        if (pokemonState == null && errorMessage.value.isEmpty()) {
            Text(
                text = "Pokémon não encontrado.",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Função de compartilhamento corrigida
fun sharePokemonName(context: Context, pokemonName: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Este é meu Pokémon favorito: $pokemonName!")
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar via"))
}
