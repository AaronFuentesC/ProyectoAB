package com.codelab.proyectoab.ui.screens


import android.content.SharedPreferences
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.codelab.proyectoab.RepositorioJugadores

@Composable
fun AppPortrait(navController: NavHostController, prefs: SharedPreferences) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentDestination = navController.currentDestination?.route

                NavigationBarItem(
                    selected = currentDestination == "inicio",
                    onClick = { navController.navigate("inicio") },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentDestination == "plantilla",
                    onClick = { navController.navigate("plantilla") },
                    label = { Text("Jugadores") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentDestination == "ajustes",
                    onClick = { navController.navigate("ajustes") },
                    label = { Text("Ajustes") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("inicio") {
                InicioScreen(navController = navController, prefs = prefs)
            }

            composable("plantilla") {
                PlantillaScreen(navController = navController, prefs = prefs)
            }

            composable("ajustes") {
                ConfiguracionScreen(navController = navController, prefs = prefs)
            }

            composable(
                "detalle_jugador/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                val jugador = RepositorioJugadores.getJugadorPorId(id ?: 0)

                if (jugador != null)
                    DetalleJugadorScreen(jugador)
                else
                    Text("Jugador no encontrado")
            }
        }
    }
}
