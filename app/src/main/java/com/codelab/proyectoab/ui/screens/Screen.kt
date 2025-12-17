package com.example.navegacion

sealed class Screen(val route: String) {
    object InicioScreen : Screen("Inicio_screen")
    object DetalleJugadorScreen : Screen("detail_screen/{id}") // Ruta con un argumento {id}
}