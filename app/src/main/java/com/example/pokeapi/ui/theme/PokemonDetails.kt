package com.example.pokeapi.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.pokeapi.network.PokemonApiResponse
import com.example.pokeapi.network.Sprites

import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder

@Composable
fun PokemonDetails(pokemon: PokemonApiResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nome
        Text(
            text = pokemon.name.replaceFirstChar { it.uppercase() },
        )

        // Imagem
        val imageUrl = pokemon.sprites.other?.dream_world?.front_default
        if (imageUrl != null) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory()) // Adiciona o suporte a SVG
                }
                .build()

            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    imageLoader = imageLoader
                ),
                contentDescription = "Imagem de ${pokemon.name}",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
        } else {
            Text(text = "Imagem não disponível", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Altura
        Text(text = "Altura: ${pokemon.height}0cm")

        // Peso
        Text(text = "Peso: ${pokemon.weight.toString().dropLast(1)}kg")

    }
}


