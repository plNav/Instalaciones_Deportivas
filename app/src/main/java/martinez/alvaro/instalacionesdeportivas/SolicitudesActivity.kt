package martinez.alvaro.instalacionesdeportivas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import martinez.alvaro.instalacionesdeportivas.databinding.ActivitySolicitudesBinding
import kotlin.collections.ArrayList

class SolicitudesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySolicitudesBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolicitudesBinding.inflate(layoutInflater)
        database = Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage
        setContentView(binding.root)
        database.collection("users").whereEqualTo("email", auth.currentUser?.email.toString()).get().addOnCompleteListener { task ->
            val userDoc = task.result?.documents?.get(0)
            val listaPeticiones = ArrayList<Usuario>()
            val peticiones = userDoc?.get("peticiones") as MutableList<String>
            database.collection("users").get().addOnCompleteListener { taskResult ->
                val usersDocuments = taskResult.result?.documents
                var cont = 0
                usersDocuments?.forEach { document ->
                    if(peticiones.contains(document.id)) {
                        val id = document.id
                        val nombre = document.get("name").toString()
                        val imgRef = document.get("image").toString()
                        val imgRefSplit = imgRef.split("/")
                        val child = if(imgRef.contains("imagenes_usuarios/")) "${imgRefSplit[imgRefSplit.size - 2]}/${imgRefSplit.last()}"
                                    else imgRefSplit.last()
                        storage.reference.child(child).downloadUrl.addOnSuccessListener { uri ->
                            listaPeticiones.add(Usuario(id, nombre, uri))
                            cont++
                            if(cont == listaPeticiones.size) {
                                binding.rvSolicitudes.layoutManager = LinearLayoutManager(this)
                                val adapter = AdaptadorSolicitud(listaPeticiones)
                                binding.rvSolicitudes.adapter = adapter
                            }
                        }
                    }
                }
            }
        }
    }
}