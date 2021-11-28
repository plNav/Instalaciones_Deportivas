package martinez.alvaro.instalacionesdeportivas

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text

class JugadorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val nombre = itemView.findViewById(R.id.txtNombreRecycler) as TextView
    val puntuacion = itemView.findViewById(R.id.txtPuntuacionRecycler) as TextView
    val imagen = itemView.findViewById(R.id.imgUsuario) as ImageView
    fun bind(jugador: Jugador) {
        nombre.text = jugador.nombre
        puntuacion.text = jugador.puntuacion
        Glide.with(itemView).load(jugador.uri).into(imagen)
    }
}

class AmigoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val nombre = itemView.findViewById(R.id.txtNombreAmigo) as TextView
    val imagen = itemView.findViewById(R.id.imgAmigo) as ImageView
    fun bind(amigo: Amigo) {
        nombre.text = amigo.nombre
        Glide.with(itemView).load(amigo.uri).into(imagen)
    }
}

class SolicitudViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var id = ""
    val nombre = itemView.findViewById(R.id.txtNombreAgregar) as TextView
    val database: FirebaseFirestore = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    val image = itemView.findViewById(R.id.imgUserSolicitud) as ImageView
    val btnAceptar = itemView.findViewById(R.id.btnAgregar) as Button
    fun bind(usuario: Usuario) {
        id = usuario.id
        nombre.text = usuario.nombre
        Glide.with(itemView).load(usuario.uri).into(image)
        btnAceptar.setOnClickListener {
            database.collection("users").whereEqualTo("email", auth.currentUser?.email.toString()).get().addOnCompleteListener {
                val doc = it.result?.documents?.get(0)
                val docId = doc?.id.toString()
                val friends = doc?.get("amigos") as MutableList<String>
                val peticiones = doc?.get("peticiones") as MutableList<String>
                friends.add(id)
                peticiones.remove(id)
                val docRef = database.collection("users").document(docId)
                docRef.update("amigos", friends)
                docRef.update("peticiones", peticiones)
                database.collection("users").document(id).get().addOnCompleteListener { senderDoc ->
                    val amigosSender = senderDoc.result?.get("amigos") as MutableList<String>
                    amigosSender.add(docId)
                    database.collection("users").document(id).update("amigos", amigosSender)
                }
            }
            Toast.makeText(itemView.context, "Solicitud aceptada", Toast.LENGTH_SHORT).show()
            itemView.visibility = View.GONE
        }
    }
}

class UsuarioViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var id = ""
    val auth: FirebaseAuth = Firebase.auth
    val nombre = itemView.findViewById(R.id.txtNombreUsuario) as TextView
    val database: FirebaseFirestore = Firebase.firestore
    val image = itemView.findViewById(R.id.imgUsuarioAdd) as ImageView
    val btnAdd = itemView.findViewById(R.id.btnSendSolicitud) as Button
    fun bind(usuario: Usuario) {
        id = usuario.id
        nombre.text = usuario.nombre
        Glide.with(itemView).load(usuario.uri).into(image)

        btnAdd.setOnClickListener {
            database.collection("users").whereEqualTo("email", auth.currentUser?.email).get().addOnCompleteListener {
                val idCurrentUser = it.result?.documents?.get(0)?.id.toString()
                database.collection("users").document(id).get().addOnCompleteListener { docOtherUser ->
                    val peticiones = docOtherUser.result?.get("peticiones") as MutableList<String>
                    if(peticiones.contains(idCurrentUser)) {
                        Toast.makeText(itemView.context, "Ya tiene una pendiente", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    peticiones.add(idCurrentUser)
                    val docUserUpdate = database.collection("users").document(id)
                    docUserUpdate.update("peticiones", peticiones)
                    Toast.makeText(itemView.context, "Solicitud enviada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

class PartidaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val auth: FirebaseAuth = Firebase.auth
    val database: FirebaseFirestore = Firebase.firestore
    val storage: FirebaseStorage = Firebase.storage
    val fecha = itemView.findViewById(R.id.txtFecha) as TextView
    val hora = itemView.findViewById(R.id.txtHora) as TextView
    val img1 = itemView.findViewById(R.id.imgJug1) as ImageView
    val img2 = itemView.findViewById(R.id.imgJug2) as ImageView
    val img3 = itemView.findViewById(R.id.imgJug3) as ImageView
    val img4 = itemView.findViewById(R.id.imgJug4) as ImageView
    var id: String = ""
    fun bind(partida: Partida) {
        id = partida.id
        fecha.text = partida.fecha
        hora.text = partida.hora
        Glide.with(itemView).load(partida.jugadores[0].uri).into(img1)
        img1.tag = partida.jugadores[0].email
        Glide.with(itemView).load(partida.jugadores[1].uri).into(img2)
        img2.tag = partida.jugadores[2].email
        Glide.with(itemView).load(partida.jugadores[2].uri).into(img3)
        img3.tag = partida.jugadores[3].email
        Glide.with(itemView).load(partida.jugadores[3].uri).into(img4)
        img4.tag = partida.jugadores[4].email
        img1.setOnClickListener {
            if(!canJoin(img1)) return@setOnClickListener
            joinMatch(img1, partida,1)
        }
        img2.setOnClickListener {
            if(!canJoin(img2)) return@setOnClickListener
            joinMatch(img2, partida,2)
        }
        img3.setOnClickListener {
            if(!canJoin(img3)) return@setOnClickListener
            joinMatch(img3, partida,3)
        }
        img4.setOnClickListener {
            if(!canJoin(img4)) return@setOnClickListener
            joinMatch(img4, partida,4)
        }
    }

    private fun joinMatch(img: ImageView, partida: Partida, pos: Int) {
        img.tag = auth.currentUser?.email
        database.collection("users").whereEqualTo("email", auth.currentUser?.email).get().addOnCompleteListener { task ->
            val doc = task.result?.documents?.get(0)
            val email = doc?.get("email").toString()
            val imgRef = doc?.get("image").toString()
            val imgSplit = imgRef.split("/")
            val imgPath = if(imgRef.contains("imagenes_usuarios/")) "${imgSplit[imgSplit.size - 2]}/${imgSplit.last()}"
                          else imgSplit.last()
            storage.reference.child(imgPath).downloadUrl.addOnSuccessListener {
                Glide.with(itemView).load(it).fitCenter().centerCrop().into(img)
                val uri = it
                val jugador = Jugador(email = email, uri = uri)
                partida.jugadores[pos].email = jugador.email
                partida.jugadores[pos].uri = jugador.uri
                database.collection("partidas").document(partida.id).get().addOnCompleteListener { taskPartida ->
                    val jugadoresPartida = taskPartida.result?.get("jugadores") as MutableList<Map<String, String>>
                    jugadoresPartida.add(mapOf(
                        "email" to email,
                        "image" to imgRef
                    ))
                    val docPartida = database.collection("partidas").document(partida.id)
                    docPartida.update("jugadores", jugadoresPartida)
                }
            }
        }
    }

    fun canJoin(img: ImageView): Boolean{
        if(isBusy(img.tag.toString())) return false
        else if(isJoined(auth.currentUser?.email.toString())){
            Toast.makeText(itemView.context, "Ya estas unido a esa partida", Toast.LENGTH_SHORT).show()
            return false   
        }
        return true
    }
    fun isBusy(tag: String): Boolean = tag != ""
    fun isJoined(email: String): Boolean = img1.tag.toString() == email || img2.tag.toString() == email || img3.tag.toString() == email || img4.tag.toString() == email
}