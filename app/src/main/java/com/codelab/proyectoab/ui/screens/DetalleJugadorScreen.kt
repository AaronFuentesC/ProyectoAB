package com.codelab.proyectoab.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import com.codelab.proyectoab.MainActivity
import com.codelab.proyectoab.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

@Composable
fun DetalleJugadorScreen(jugador: Jugador) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        DetalleJugadorHorizontal(jugador)
    } else {
        DetalleJugadorVertical(jugador)
    }
}

@Composable
fun DetalleJugadorVertical(jugador: Jugador) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(115.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            jugador?.let {
                Image(
                    painter = painterResource(id = it.imagenId),
                    contentDescription = "Foto del jugador",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                jugador?.let {
                    Image(
                        painter = painterResource(id = it.imagenId),
                        contentDescription = "icono",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(6.dp)
                    )

                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = jugador?.nombre ?: "Sin nombre",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FotosYEstadisticas(jugador)

            Spacer(modifier = Modifier.height(12.dp))

            DatosPersonales(jugador)

            AccionesJugador(jugador = jugador, context = context)
            Spacer(Modifier.height(16.dp))

            jugador?.let { BotonNotificacion(it) }

        }

        Text(
            text = "Nuevo",
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DetalleJugadorHorizontal(jugador: Jugador) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            jugador?.let {
                Image(
                    painter = painterResource(id = it.imagenId),
                    contentDescription = "Foto del jugador",
                    modifier = Modifier
                        .weight(0.45f)
                        .height(300.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(0.55f)) {
                Text(
                    text = jugador?.nombre ?: "Sin nombre",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatosPersonales(jugador)
                Spacer(modifier = Modifier.height(8.dp))
                FotosYEstadisticas(jugador)
                AccionesJugador(jugador = jugador, context = context)
                Spacer(Modifier.height(16.dp))
                jugador?.let { BotonNotificacion(it) }
            }
        }

        Text(
            text = "Nuevo",
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
fun FotosYEstadisticas(jugador: Jugador?) {
    Column {
        jugador?.let {
            Icon(
                painter = painterResource(id = it.imagenId),
                contentDescription = "icono jugador",
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Partidos: ${jugador?.partidos ?: 0}", color = MaterialTheme.colorScheme.onBackground)
        Text("Goles: ${jugador?.goles ?: 0}", color = MaterialTheme.colorScheme.onBackground)
        Text("Asistencias: ${jugador?.asistencias ?: 0}", color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun DatosPersonales(jugador: Jugador?) {
    Column {
        Text("Nombre: ${jugador?.nombre ?: "-"}", color = MaterialTheme.colorScheme.onBackground)
        Text("Posición: ${jugador?.posicion?.toLocalizedString() ?: "-"}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Edad: ${jugador?.edad ?: "-"} años", color = MaterialTheme.colorScheme.onBackground)
        Text("Altura: ${jugador?.altura ?: "-"} m", color = MaterialTheme.colorScheme.onBackground)
        Text("Peso: ${jugador?.peso ?: "-"} kg", color = MaterialTheme.colorScheme.onBackground)
        Text("País: ${jugador?.pais ?: "-"}", color = MaterialTheme.colorScheme.onBackground)
    }
}
@Composable
fun BotonNotificacion(jugador: Jugador){
    val context = LocalContext.current
    Button(
        onClick = {
            val mainActivity = context as MainActivity
            if (mainActivity.tienePermisoNotificaciones()) {
                mainActivity.mostrarNotificacionJugador(
                    jugador,
                    "¡Logro del día!",
                    "${jugador.nombre} ha marcado su tercer gol consecutivo"
                )
            } else {
                mainActivity.solicitarPermisoNotificaciones()
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Notificar logro")
    }
}

@Composable
fun AccionesJugador(jugador: Jugador, context: Context) {
    Button(
        onClick = {
            val url = jugador.urlPerfil.trim()
            if (url.isNotBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "No hay navegador instalado",
                        Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "URL no disponible para este jugador",
                    Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Ver web del jugador")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = {
            val mensaje = "¡Mira a ${jugador.nombre} (${jugador.posicion}) del Albacete Balompié!" + "\n${jugador.urlPerfil}"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, mensaje)
            }
            context.startActivity(Intent.createChooser(intent, "Compartir con..."))
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Compartir ficha")
    }
}
