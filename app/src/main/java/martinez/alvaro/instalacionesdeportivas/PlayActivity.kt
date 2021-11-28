package martinez.alvaro.instalacionesdeportivas

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityPlayBinding
import kotlin.math.log

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        database = Firebase.firestore
        storage = Firebase.storage
        setContentView(binding.root)
        binding.btnCrearPartida.setOnClickListener { crearPartida() }

        var listaPartidas = ArrayList<Partida>()
        database.collection("partidas").get().addOnSuccessListener { documents ->
            documents.forEach { document ->
                val id = document.id
                val fecha = document["fecha"] as String
                val hora = document["hora"] as String
                val jugadores = document["jugadores"] as List<Map<String, String>>
                val listaJugadores = ArrayList<Jugador>()
                var cont = 0
                jugadores.forEach { jugador ->

                    val email = jugador["email"].toString()
                    val imgStoragePath = jugador["image"].toString()
                    val imgPathSplit = imgStoragePath?.split("/")
                    val child = if(imgStoragePath.contains("imagenes_usuarios/")) "${imgPathSplit[imgPathSplit.size - 2]}/${imgPathSplit.last()}"
                                else imgPathSplit.last()

                    storage.reference.child(child).downloadUrl.addOnSuccessListener { uri ->
                        listaJugadores.add(Jugador(email = email, uri = uri))
                    }.addOnCompleteListener {
                        cont++
                        if(cont == jugadores.size) {
                            if(listaJugadores.size <= 4){
                                storage.reference.child("icono_agregar.png").downloadUrl.addOnSuccessListener { uri2 ->
                                    for(i in 0..(4 - listaJugadores.size)) listaJugadores.add(Jugador(email = "", uri = uri2))
                                }.addOnCompleteListener {
                                    listaPartidas.add(Partida(fecha, hora, listaJugadores, id))
                                    binding.rvPartidas.layoutManager = LinearLayoutManager(this)
                                    val adapter = AdaptadorPartida(listaPartidas)
                                    binding.rvPartidas.adapter = adapter
                                }
                            }else {
                                listaPartidas.add(Partida(fecha, hora, listaJugadores, id))
                                binding.rvPartidas.layoutManager = LinearLayoutManager(this)
                                val adapter = AdaptadorPartida(listaPartidas)
                                binding.rvPartidas.adapter = adapter
                            }
                        }
                    }
                }
            }
        }
    }

    private fun crearPartida() {
        val intent = Intent(this, CrearPartidaActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }

}