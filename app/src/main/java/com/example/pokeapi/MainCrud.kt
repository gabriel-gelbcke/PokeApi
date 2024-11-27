package com.example.pokeapi

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokeapi.Database.AppDatabase
import com.example.pokeapi.Entitites.Pokemon
import com.example.pokeapi.Entitites.Treinador
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCrud(navController: NavController) {
    // Use `LocalContext` para pegar o contexto do App
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val pokemonDao = remember { database.pokemonDao() }

    // Use o escopo de corrotina da composição
    val coroutineScope = rememberCoroutineScope()

    var pokemonName by remember { mutableStateOf("") }
    val pokemons = remember { mutableStateListOf<Pokemon>() }

    // Carregar dados do banco de dados
    LaunchedEffect(Unit) {
        // Carrega os Pokémons do banco de dados quando a composição é criada
        pokemons.clear()
        pokemons.addAll(pokemonDao.getAll())
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically, // Alinha verticalmente o ícone e o texto
                horizontalArrangement = Arrangement.Start, // Alinha no início da linha
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botão para voltar para a tela principal (seta)
                IconButton(
                    onClick = { navController.popBackStack() }, // Vai voltar para a tela anterior
                    modifier = Modifier
                        .size(width = 85.dp, height = 50.dp) // Define o tamanho do botão
                        .padding(end = 8.dp) // Espaço entre o ícone e o texto
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Alinha verticalmente o ícone e o texto
                        horizontalArrangement = Arrangement.Start, // Alinha no início da linha
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "HOME",
                            tint = Color.White // Cor do ícone
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        Text(
                            "HOME",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterVertically) // Garante que o texto se alinha ao centro verticalmente
                        )
                    }
                }
            }

            OutlinedTextField(
                value = pokemonName,
                onValueChange = { pokemonName = it.trim() }, // Remove espaços extras
                label = {
                    Text(
                        text = "Pokémons já capturados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White // Cor do texto do label
                    )
                },
                placeholder = {
                    Text(
                        text = "Nome do pokemon",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f) // Placeholder com opacidade
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

//            Button(
//                onClick = {
//                    if (pokemonName.isNotEmpty()) {
//                        val newPokemon = Pokemon(nome = pokemonName)
//                        pokemons.add(newPokemon)
//                        pokemonName = ""
//
//                        // Insere no banco de dados dentro de uma corrotina
//                        coroutineScope.launch {
//                            pokemonDao.insert(newPokemon)
//                        }
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Adicionar Pokémon", color = Color.White)
//            }

            Button(
                onClick = {
                    if (pokemonName.isNotEmpty()) {
                        val newPokemon = Pokemon(nome = pokemonName)
                        pokemons.add(newPokemon)
                        pokemonName = ""

                        // Insere no banco de dados dentro de uma corrotina
                        coroutineScope.launch {
                            pokemonDao.insert(newPokemon)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
            ) {
                Text("Adicionar Pokémon")
            }







            // Lista de Pokémons
            LazyColumn(modifier = Modifier.fillMaxHeight(0.4f)) {
                items(pokemons) { pokemon ->
                    ListItem(
                        headlineContent = { Text(pokemon.nome, color = Color.Black) },
                        trailingContent = {
                            IconButton(onClick = {
                                pokemons.remove(pokemon)

                                // Remove do banco de dados dentro de uma corrotina
                                coroutineScope.launch {
                                    pokemonDao.delete(pokemon)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Remover Pokémon",
                                    tint = Color.Red
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
