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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
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
                    .size(width = 80.dp, height = 50.dp) // Define o tamanho do botão
                    .padding(end = 8.dp) // Espaço entre o ícone e o texto
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Alinha verticalmente o ícone e o texto
                    horizontalArrangement = Arrangement.Start, // Alinha no início da linha
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "CRUD",
                        tint = Color.White // Cor do ícone
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
                    Text(
                        "CRUD",
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

@OptIn(ExperimentalMaterial3Api::class)
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
//            .verticalScroll(scrollState) // Adiciona rolagem
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = busca.value,
            onValueChange = { busca.value = it.trim() },
            label = {
                Text(
                    text = "Nome do Pokémon",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White // Cor do texto do label
                )
            },
            placeholder = {
                Text(
                    text = "Digite o nome...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f) // Placeholder com opacidade
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search, // Ícone de busca
                    contentDescription = "Ícone de busca",
                    tint = Color.Cyan // Cor do ícone
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Cyan, // Cor do cursor azul claro
                focusedBorderColor = Color.Cyan, // Cor da borda quando focado
                unfocusedBorderColor = Color.Cyan.copy(alpha = 0.5f), // Cor da borda quando desfocado
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White), // Define a cor do texto digitado
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaço uniforme entre os botões
        ) {
            Button(
                onClick = {
                    errorMessage.value = ""
                    if (busca.value.isNotBlank()) {
                        viewModel.getPokemonByName(busca.value.lowercase())
                    } else {
                        errorMessage.value = "Digite um nome válido!"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Cyan, // Fundo do botão
                    contentColor = Color.White // Cor do texto do botão
                ),
                shape = RoundedCornerShape(12.dp), // Bordas arredondadas como o TextField
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp), // Elevação sutil
                modifier = Modifier
                    .weight(1f) // Divide o espaço disponível igualmente entre os botões
                    .height(64.dp) // Altura alinhada ao TextField
            ) {
                Text(
                    text = "Pesquisar",
                    style = MaterialTheme.typography.bodyMedium // Tipografia consistente
                )
            }

            pokemonState?.let { pokemon ->
                Button(
                    onClick = {
                        val pokemonName = pokemon.name.capitalize()
                        sharePokemonName(context, pokemonName)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan, // Fundo do botão
                        contentColor = Color.White // Cor do texto do botão
                    ),
                    shape = RoundedCornerShape(12.dp), // Bordas arredondadas como o TextField
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp), // Elevação sutil
                    modifier = Modifier
                        .weight(1f) // Divide o espaço disponível igualmente entre os botões
                        .height(64.dp) // Altura alinhada ao TextField
                ) {
                    Text(
                        text = "Compartilhar",
                        style = MaterialTheme.typography.bodyMedium // Tipografia consistente
                    )
                }
            }
        }

//        if (errorMessage.value.isNotEmpty()) {
//            Text(
//                text = errorMessage.value,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }

        pokemonState?.let { pokemon ->
            PokemonDetails(pokemon = pokemon)
        }


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
