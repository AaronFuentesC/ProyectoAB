package com.codelab.proyectoab.ui.screens


import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.codelab.proyectoab.RepositorioJugadores
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codelab.proyectoab.ui.screens.DetalleJugadorScreen
import com.codelab.proyectoab.ui.screens.InicioScreen
import com.codelab.proyectoab.ui.screens.PlantillaScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController


@Composable
fun AppLandscape(navController: NavHostController, prefs: SharedPreferences){
    Row(modifier = Modifier.fillMaxSize()) {

        NavigationRail {
            NavigationRailItem(
                selected = navController.currentDestination?.route == "inicio",
                onClick = { navController.navigate("inicio") },
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text("Home") }
            )
            NavigationRailItem(
                selected = navController.currentDestination?.route == "plantilla",
                onClick = { navController.navigate("plantilla") },
                icon = { Icon(Icons.Default.Person, null) },
                label = { Text("Jugadores") }
            )
            NavigationRailItem(
                selected = navController.currentDestination?.route == "ajustes",
                onClick = { navController.navigate("ajustes") },
                icon = { Icon(Icons.Default.Settings, null) },
                label = { Text("Ajustes") }
            )
        }
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.weight(1f)
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

