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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
    val treinadorDao = remember { database.treinadorDao() }

    // Use o escopo de corrotina da composição
    val coroutineScope = rememberCoroutineScope()

    var pokemonName by remember { mutableStateOf("") }
    var treinadorName by remember { mutableStateOf("") }

    val pokemons = remember { mutableStateListOf<Pokemon>() }
    val treinadores = remember { mutableStateListOf<Treinador>() }

    var isEditingPokemon by remember { mutableStateOf<Pokemon?>(null) } // Para Pokémon em modo de edição
    var isEditingTreinador by remember { mutableStateOf<Treinador?>(null) } // Para Treinador em modo de edição

    // Carregar dados do banco de dados
    LaunchedEffect(Unit) {
        // Carrega os Pokémons e Treinadores do banco de dados quando a composição é criada
        pokemons.clear()
        pokemons.addAll(pokemonDao.getAll())

        treinadores.clear()
        treinadores.addAll(treinadorDao.getAll())
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

            // Botão de Voltar
            Row(
                verticalAlignment = Alignment.CenterVertically, // Alinha verticalmente o ícone e o texto
                horizontalArrangement = Arrangement.Start, // Alinha no início da linha
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botão para voltar para a tela principal (seta)
                IconButton(
                    onClick = { navController.popBackStack() }, // Vai voltar para a tela anterior
                    modifier = Modifier
                        .size(width = 75.dp, height = 50.dp) // Define o tamanho do botão
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
                        Spacer(modifier = Modifier.weight(0.3f))
                        Text(
                            "CRUD",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterVertically) // Garante que o texto se alinha ao centro verticalmente
                        )
                    }
                }
            }

            // Adicionar ou Editar Pokémon
            OutlinedTextField(
                value = pokemonName,
                onValueChange = { pokemonName = it.trim() },
                label = { Text("Pokémons já capturados", color = Color.White) },
                placeholder = { Text("Nome do Pokémon", color = Color.White.copy(alpha = 0.6f)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.Cyan,
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Cyan.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(color = Color.White), // Defina a cor do texto aqui
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp)
            )


            Button(
                onClick = {
                    if (pokemonName.isNotEmpty()) {
                        if (isEditingPokemon != null) {
                            // Edita o Pokémon
                            val updatedPokemon = isEditingPokemon!!.copy(nome = pokemonName)
                            coroutineScope.launch {
                                pokemonDao.update(updatedPokemon)
                            }
                            // Remove da lista e adiciona o Pokémon atualizado
                            pokemons.remove(isEditingPokemon)
                            pokemons.add(updatedPokemon)
                            isEditingPokemon = null // Resetando o modo de edição
                        } else {
                            // Adiciona o Pokémon
                            val newPokemon = Pokemon(nome = pokemonName)
                            pokemons.add(newPokemon)
                            coroutineScope.launch {
                                pokemonDao.insert(newPokemon)
                            }
                        }
                        pokemonName = "" // Limpa o campo de nome
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
            ) {
                Text(if (isEditingPokemon != null) "Salvar Pokémon" else "Adicionar Pokémon")
            }

            // Adicionar ou Editar Treinador
            OutlinedTextField(
                value = treinadorName,
                onValueChange = { treinadorName = it.trim() },
                label = { Text("Treinadores já derrotados", color = Color.White) },
                placeholder = { Text("Nome do Treinador", color = Color.White.copy(alpha = 0.6f)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.Cyan,
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Cyan.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(color = Color.White), // Defina a cor do texto aqui
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp)
            )


            Button(
                onClick = {
                    if (treinadorName.isNotEmpty()) {
                        if (isEditingTreinador != null) {
                            // Edita o Treinador
                            val updatedTreinador = isEditingTreinador!!.copy(nome = treinadorName)
                            coroutineScope.launch {
                                treinadorDao.update(updatedTreinador)
                            }
                            // Remove da lista e adiciona o Treinador atualizado
                            treinadores.remove(isEditingTreinador)
                            treinadores.add(updatedTreinador)
                            isEditingTreinador = null // Resetando o modo de edição
                        } else {
                            // Adiciona o Treinador
                            val newTreinador = Treinador(nome = treinadorName)
                            treinadores.add(newTreinador)
                            coroutineScope.launch {
                                treinadorDao.insert(newTreinador)
                            }
                        }
                        treinadorName = "" // Limpa o campo de nome
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
            ) {
                Text(if (isEditingTreinador != null) "Salvar Treinador" else "Adicionar Treinador")
            }

            // Lista de Pokémons
            Text("Pokémons", color = Color.Cyan, style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.fillMaxHeight(0.47f) .fillMaxWidth(0.9f)) {
                items(pokemons) { pokemon ->
                    ListItem(
                        headlineContent = { Text(pokemon.nome, color = Color.Black) },
                        trailingContent = {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = {
                                    pokemons.remove(pokemon)
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
                                IconButton(onClick = {
                                    pokemonName = pokemon.nome
                                    isEditingPokemon = pokemon // Marca o Pokémon para edição
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar Pokémon",
                                        tint = Color.Yellow
                                    )
                                }
                            }
                        }
                    )
                }
            }

            // Lista de Treinadores
            Text("Treinadores", color = Color.Cyan, style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.fillMaxHeight() .fillMaxWidth(0.9f)) {
                items(treinadores) { treinador ->
                    ListItem(
                        headlineContent = { Text(treinador.nome, color = Color.Black) },
                        trailingContent = {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = {
                                    treinadores.remove(treinador)
                                    coroutineScope.launch {
                                        treinadorDao.delete(treinador)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Remover Treinador",
                                        tint = Color.Red
                                    )
                                }
                                IconButton(onClick = {
                                    treinadorName = treinador.nome
                                    isEditingTreinador = treinador // Marca o Treinador para edição
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar Treinador",
                                        tint = Color.Yellow
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}