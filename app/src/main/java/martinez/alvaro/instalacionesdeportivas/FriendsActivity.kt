package martinez.alvaro.instalacionesdeportivas

import android.content.Intent
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
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityAddFriendBinding
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityFriendsBinding

class FriendsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFriendsBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendsBinding.inflate(layoutInflater)
        database = Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage
        setContentView(binding.root)

        val listaAmigos = ArrayList<Amigo>()


        database.collection("users").whereEqualTo("email", auth.currentUser?.email.toString()).get().addOnCompleteListener { task ->
            val friends = task.result?.documents?.get(0)?.get("amigos") as ArrayList<String>
            var cont = 0
            friends.forEach { friend ->
                database.collection("users").document(friend).get().addOnCompleteListener { friendTask ->
                    val friendResult = friendTask.result
                    val name = friendResult?.get("name").toString()
                    val imgRef = friendResult?.get("image").toString()
                    val imgRefSplit = imgRef.split("/")
                    val child = if(imgRef.contains("imagenes_usuarios/")) "${imgRefSplit[imgRefSplit.size - 2]}/${imgRefSplit.last()}"
                                else imgRefSplit.last()
                    storage.reference.child(child).downloadUrl.addOnSuccessListener { uri ->
                        cont++
                        listaAmigos.add(Amigo(name, uri))
                        if(cont == friends.size) {
                            binding.rvAmigos.layoutManager = LinearLayoutManager(this)
                            val adapter = AdaptadorAmigo(listaAmigos)
                            binding.rvAmigos.adapter = adapter
                        }
                    }
                }
            }
        }



        binding.btnAddFriend.setOnClickListener { addFriend() }
        binding.btnSolicitudesAmistad.setOnClickListener { solicitudesAmistad() }
    }

    private fun solicitudesAmistad() {
        val intent = Intent(this, SolicitudesActivity::class.java)
        this.startActivity(intent)
    }

    private fun addFriend() {
        val intent = Intent(this, AddFriendActivity::class.java)
        this.startActivity(intent)
    }
}