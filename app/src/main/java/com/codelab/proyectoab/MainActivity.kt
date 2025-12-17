package com.codelab.proyectoab

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.codelab.proyectoab.ui.screens.Jugador
import com.codelab.proyectoab.ui.theme.ProyectoABTheme
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.codelab.proyectoab.ui.screens.AppRoot

class MainActivity : ComponentActivity() {

    companion object {
        const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
        const val CLAVE_TEMA_OSCURO = "tema_oscuro"
        const val CLAVE_JUGADORES_EXPANDIDOS = "jugadores_expandidos"
        const val EXTRA_JUGADOR = "jugador_seleccionado"
        const val EXTRA_JUGADOR_ID = "jugador_id"
    }

    // Para restaurar navegación
    private var jugadorIdParaRestaurar: Int = -1

    // Launcher para escoger jugador
    val seleccionJugadorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val jugador = result.data?.getParcelableExtra<Jugador>(EXTRA_JUGADOR)
            jugador?.let {
                Toast.makeText(this, "Seleccionado: ${it.nombre}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crearCanalNotificaciones()

        // SharedPreferences
        val prefs = getSharedPreferences("ajustes_usuario", Context.MODE_PRIVATE)
        val temaOscuroGuardado = prefs.getBoolean(CLAVE_TEMA_OSCURO, false)

        // Aplicar tema ANTES de setContent
        AppCompatDelegate.setDefaultNightMode(
            if (temaOscuroGuardado) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Recuperar id desde notificación o savedInstanceState
        jugadorIdParaRestaurar =
            savedInstanceState?.getInt("jugador_restore")
                ?: intent?.getIntExtra(EXTRA_JUGADOR_ID, -1)
                        ?: -1

        enableEdgeToEdge()

        // Detectar deep link (ej: https://www.albacetebalompie.es/jugadores/1)
        val data = intent?.data
        val jugadorIdDesdeDeepLink = data?.let { uri ->
            if (uri.host == "www.albacetebalompie.es" && uri.path?.startsWith("/jugadores/") == true) {
                uri.lastPathSegment?.toIntOrNull() ?: -1
            } else -1
        } ?: -1

        setContent {
            ProyectoABTheme(darkTheme = temaOscuroGuardado) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ⬅ Aquí va ahora el root de la navegación adaptativa
                    AppRoot(
                        prefs = prefs,
                        initialJugadorId = jugadorIdParaRestaurar
                    )
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("jugador_restore", jugadorIdParaRestaurar)
    }

    // ==== PERMISOS DE CONTACTOS ====

    private val requestContactPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Permiso necesario para ver contactos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tienePermisoContactos(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

    fun solicitarPermisoContactos() {
        requestContactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    fun usuarioTienePermisoContactos(): Boolean = tienePermisoContactos()

    // ==== PERMISOS DE NOTIFICACIONES ====

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (!isGranted) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    Toast.makeText(
                        this,
                        "Las notificaciones permiten avisarte de eventos del equipo",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Activa las notificaciones en Ajustes",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivity(intent)
                }
            }
        }

    fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !tienePermisoNotificaciones()
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun tienePermisoNotificaciones(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

    // ==== NOTIFICACIONES ====

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                "CANAL_JUGADORES",
                "Eventos del Albacete Balompié",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones sobre goles, lesiones y logros"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }


    fun mostrarNotificacionJugador(jugador: Jugador, titulo: String, mensaje: String) {

        if (!tienePermisoNotificaciones()) {
            Toast.makeText(this, "Permiso de notificaciones requerido", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_JUGADOR_ID, jugador.id)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            jugador.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "CANAL_JUGADORES")
            .setSmallIcon(R.drawable.ic_albacete)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(jugador.id, builder.build())
    }
}
