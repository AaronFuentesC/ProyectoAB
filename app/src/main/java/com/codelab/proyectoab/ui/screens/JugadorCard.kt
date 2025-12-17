package com.codelab.proyectoab.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.ui.unit.sp

@Composable
fun JugadorCard(
    jugador: Jugador,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onExpandedChange() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen circular
                Image(
                    painter = painterResource(id = jugador.imagenId),
                    contentDescription = "Foto jugador",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(text = jugador.nombre, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    Text(text = "Dorsal: ${jugador.dorsal}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Posición: ${jugador.posicion.toLocalizedString()}", color = MaterialTheme.colorScheme.onBackground)
                Text("Edad: ${jugador.edad} años", color = MaterialTheme.colorScheme.onBackground)
                Text("Altura: ${jugador.altura} m", color = MaterialTheme.colorScheme.onBackground)
                Text("Peso: ${jugador.peso} kg", color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { navController.navigate("detalle_jugador/${jugador.id}") }) {
                        Text("Ver")
                    }
                }
            }
        }
    }
}
