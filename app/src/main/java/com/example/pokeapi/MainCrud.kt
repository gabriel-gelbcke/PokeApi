package com.example.pokeapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCrud(navController: NavController) {
    // Estados para armazenar os valores inseridos
    var pokemonName by remember { mutableStateOf("") }
    var trainerName by remember { mutableStateOf("") }

    // Listas para armazenar Pokémons e treinadores
    var pokemonList by remember { mutableStateOf(listOf<String>()) }
    var trainerList by remember { mutableStateOf(listOf<String>()) }

    // Surface com plano de fundo preto
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

            OutlinedTextField(
                value = trainerName,
                onValueChange = { trainerName = it.trim() }, // Remove espaços extras
                label = {
                    Text(
                        text = "Treinadores já derrotados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White // Cor do texto do label
                    )
                },
                placeholder = {
                    Text(
                        text = "Nome do treinador",
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

            Spacer(modifier = Modifier.height(0.dp))

            // Botão para adicionar Pokémon à lista
            Button(
                onClick = {
                    if (pokemonName.isNotEmpty()) {
                        pokemonList = pokemonList + pokemonName
                        pokemonName = "" // Limpar o campo após adicionar
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

            Spacer(modifier = Modifier.height(8.dp))

            // Botão para adicionar Treinador à lista
            Button(
                onClick = {
                    if (trainerName.isNotEmpty()) {
                        trainerList = trainerList + trainerName
                        trainerName = "" // Limpar o campo após adicionar
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
            ) {
                Text("Adicionar Treinador")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Exibindo a lista de Pokémons e Treinadores
            if (pokemonList.isNotEmpty()) {
                Text("Pokémons Capturados:", color = Color.White)
                pokemonList.forEach { name ->
                    Text("- $name", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (trainerList.isNotEmpty()) {
                Text("Treinadores Derrotados:", color = Color.White)
                trainerList.forEach { name ->
                    Text("- $name", color = Color.White)
                }
            }
        }
    }
}