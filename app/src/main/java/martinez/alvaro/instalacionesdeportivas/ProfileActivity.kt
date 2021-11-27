package martinez.alvaro.instalacionesdeportivas

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityProfileBinding
import java.io.File
import java.net.URI
import kotlin.math.log

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var path: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = Firebase.firestore
        storage = Firebase.storage
        setup()

        binding.imgUser.setOnClickListener {
            Toast.makeText(applicationContext, "${binding.imgUser.height}x${binding.imgUser.width}", Toast.LENGTH_SHORT).show()
            var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/")
            startActivityForResult(Intent.createChooser(intent,"Seleccione la Aplicación"),10)
        }

        binding.btnSave.setOnClickListener {
            if(checkInputs()){
                database.collection("users").whereEqualTo("email", auth.currentUser?.email)
                .get().addOnCompleteListener { task ->

                    val id = task.result?.documents?.get(0)?.id
                    val user = hashMapOf(
                        "email" to binding.inputEmailProfile.text.toString(),
                        "name" to binding.inputNameProfile.text.toString(),
                        "phone" to binding.inputPhoneProfile.text.toString(),
                        "date" to binding.inputFechaNac.text.toString()
                    )

                    if(path != null) {
                        val storageRef = storage.reference
                        val imageRef = storageRef.child("imagenes_usuarios/${path.lastPathSegment}")
                        val img = storage.getReferenceFromUrl(storageRef.child("imagenes_usuarios/${path.lastPathSegment}").toString())
                        imageRef.putFile(path).addOnSuccessListener {
                            Toast.makeText(applicationContext, "Imagen subida", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext, "Imagen no subida", Toast.LENGTH_SHORT).show()
                        }
                        user["image"] = img.toString()
                    }

                    val document = database.collection("users").document(id?: "")
                    user.forEach { data -> document.update(data.key, data.value.toString()) }
                    Toast.makeText(applicationContext, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                }
            }else Toast.makeText(applicationContext, "Algun campo no es correcto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            path = data?.data!!
            binding.imgUser.setImageURI(path)
        }
    }


    private fun checkInputs(): Boolean = checkEmail(binding.inputEmailProfile.text.toString()) &&
                                         binding.inputNameProfile.text.toString().isNotBlank() &&
                                         checkPhone(binding.inputPhoneProfile.text.toString()) &&
                                         checkDate(binding.inputFechaNac.text.toString())

    private fun checkPhone(phone: String): Boolean = phone.isNullOrBlank() ||
                                                     ((phone.toIntOrNull() != null) && phone.length == 9)

    private fun checkDate(date: String): Boolean = date.isNullOrBlank() ||
                                                   (date.matches("""([1-9]|0[1-9]|[12][0-9]|3[01])/([0-9]|[0][0-9]|1[012])/\d{4}""".toRegex()))

    private fun checkEmail(email: String): Boolean = email.isNotBlank() &&
                                                     android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()


    private fun setup() {
        database.collection("users").whereEqualTo("email", auth.currentUser?.email)
        .get().addOnCompleteListener {
            val documentUser = it.result?.documents?.get(0)
            Log.d("USER_", documentUser.toString())
            binding.inputEmailProfile.setText(documentUser?.get("email").toString())
            binding.inputNameProfile.setText(documentUser?.get("name").toString())
            binding.inputPhoneProfile.setText(documentUser?.get("phone").toString())
            binding.inputFechaNac.setText(documentUser?.get("date").toString())
            binding.txtPuntuacionPerfil.text = "Score: ${documentUser?.get("puntuacion").toString()}"
            var imgStoragePath = documentUser?.get("image").toString()
            var pathSplited = imgStoragePath.split("/")
            var child = if(imgStoragePath.contains("imagenes_usuarios/")) "${pathSplited[pathSplited.size - 2]}/${pathSplited.last()}"
                        else imgStoragePath.split("/").last()
            storage.reference.child(child).downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this).load(uri).fitCenter().centerCrop().into(binding.imgUser)
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}