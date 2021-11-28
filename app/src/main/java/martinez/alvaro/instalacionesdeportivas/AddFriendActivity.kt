package martinez.alvaro.instalacionesdeportivas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityAddFriendBinding

class AddFriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFriendBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        database = Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage
        setContentView(binding.root)

        var documents: List<DocumentSnapshot> = listOf()

        database.collection("users").get().addOnCompleteListener {
            documents = it.result?.documents ?: listOf()
        }

        var friendsUser = ArrayList<String>()
        database.collection("users").whereEqualTo("email", auth.currentUser?.email.toString()).get().addOnCompleteListener {
            friendsUser = it.result?.documents?.get(0)?.get("amigos") as ArrayList<String>
        }

        val listaUsuarios= mutableListOf<Usuario>()

        binding.btnBuscar.setOnClickListener {
            
            var cont = 0
            documents.forEach { document ->
                val id = document.id
                val nombre = document["name"].toString()
                if(!nombre.contains(binding.inputNombre.text?: "") || document["email"].toString() == auth.currentUser?.email || friendsUser.contains(id)) {
                    cont++
                    return@forEach
                }

                val imageRef = document["image"].toString()
                val imageRefSplit = imageRef.split("/")
                val imageChild = if(imageRef.contains("imagenes_usuarios/")) "${imageRefSplit[imageRefSplit.size - 2]}/${imageRefSplit.last()}"
                                 else imageRefSplit.last()
                storage.reference.child(imageChild).downloadUrl.addOnSuccessListener { uri ->
                    cont++
                    val usuario = Usuario(id, nombre, uri)
                    listaUsuarios.add(usuario)
                    if(cont == documents.size) {
                        binding.rvListaUsuarios.layoutManager = LinearLayoutManager(this)
                        val adapter = AdaptadorUsuario(listaUsuarios)
                        binding.rvListaUsuarios.adapter = adapter
                    }
                }
            }
        }
    }
}