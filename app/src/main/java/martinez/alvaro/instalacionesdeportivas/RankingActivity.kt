package martinez.alvaro.instalacionesdeportivas

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
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRankingBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        database = Firebase.firestore
        storage = Firebase.storage
        setContentView(binding.root)

        var listaJugadores = ArrayList<Jugador>()
        database.collection("users").get().addOnSuccessListener {
            for(doc in it) {
                val nombre = doc["name"]
                val puntuacion = doc["puntuacion"]

                val imgStoragePath = doc?.get("image").toString()
                var pathSplited = imgStoragePath.split("/")
                val child = if(imgStoragePath.contains("imagenes_usuarios/")) "${pathSplited[pathSplited.size - 2]}/${pathSplited.last()}"
                            else imgStoragePath.split("/").last()
                Log.d("CHILD", child)
                storage.reference.child(child).downloadUrl.addOnSuccessListener { uri ->
                    val uri = uri
                    listaJugadores.add(Jugador(nombre.toString(), uri, puntuacion.toString()))
                }.addOnCompleteListener {
                    listaJugadores.sortBy { player -> player.puntuacion.toFloat() }
                    listaJugadores.reverse()
                    binding.rvJugadores.layoutManager = LinearLayoutManager(this)
                    val adapter = AdaptadorJugador(listaJugadores)
                    binding.rvJugadores.adapter = adapter
                }
            }
        }



    }
}