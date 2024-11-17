package com.example.pokeapi.ui.theme

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Nome
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
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
                        .clip(RoundedCornerShape(8.dp)) // Borda arredondada na imagem
//                      .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)) // Borda na imagem
                )
            } else {
                Text(text = "Imagem não disponível", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Altura
            Text(
                text = "Altura: ${pokemon.height}0cm",
                modifier = Modifier.padding(vertical = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White // Usa a cor de texto preto para contraste
            )

            // Peso
            Text(
                text = "Peso: ${pokemon.weight.toString().dropLast(1)}kg",
                modifier = Modifier.padding(vertical = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            // Exibe os tipos do Pokémon
            Text(
                text = "Tipo: ${pokemon.types.joinToString(", ") { it.type.name.capitalize() }}",
                modifier = Modifier.padding(vertical = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            // Exibe as habilidades
            Text(
                text = "Habilidades: ${pokemon.abilities.joinToString(", ") { it.ability.name.capitalize() }}",
                modifier = Modifier.padding(vertical = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exibe as estatísticas do Pokémon (HP, Attack, Defense, etc.)
            Text(
                text = "Estatísticas:",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            pokemon.stats.forEach {
                Text(
                    text = "${it.stat.name.capitalize()}: ${it.base_stat}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}

