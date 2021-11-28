package martinez.alvaro.instalacionesdeportivas

import android.net.Uri

data class Jugador (val nombre: String = "", var uri: Uri, val puntuacion: String = "", var email: String = "")

data class Partida (val fecha: String, val hora: String, val jugadores: List<Jugador>, val id: String = "")

data class Usuario (val id: String, val nombre: String, val uri: Uri)

data class Amigo (val nombre: String, val uri: Uri)