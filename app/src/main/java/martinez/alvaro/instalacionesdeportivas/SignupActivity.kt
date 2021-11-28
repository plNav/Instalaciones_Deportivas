package martinez.alvaro.instalacionesdeportivas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import martinez.alvaro.instalacionesdeportivas.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = Firebase.firestore

        val opciones = listOf("1.00", "1.50", "2.00", "2.50", "3.00", "3.50", "4.00", "4.50", "5.00")

        binding.spinnerPuntuacion.adapter = ArrayAdapter(this, R.layout.spinner_item_putuacion, opciones)

        binding.btnSignUp.setOnClickListener {
            try {
                clearErrors()
                signUp( binding.inputEmailSignUp.text.toString(),
                    binding.inputPasswordSignUp.text.toString(),
                    binding.inputNameSignUp.text.toString() )
            }catch (ex: NotValidEmail) { setError(binding.inputEmailSignUp, ex.message.toString()) }
            catch (ex: NotValidPassword) { setError(binding.inputPasswordSignUp, ex.message.toString()) }
            catch (ex: NotValidName) { setError(binding.inputNameSignUp, ex.message.toString()) }
        }

    }

    private fun setError(view: EditText, message: String) {
        view.error = message
        Toast.makeText(applicationContext, "Credentials error.", Toast.LENGTH_SHORT).show()
    }

    private fun clearErrors() {
        binding.txtEmailError.visibility = View.GONE
        binding.txtPasswordError.visibility = View.GONE
        binding.txtNameError.visibility = View.GONE
    }

    private fun signUp(email: String, password: String, name: String) {

        if(!checkEmail(email)) throw NotValidEmail("Email is not valid: example@example.com")
        else if(!checkPassword(password))
            if(password.length < 6) throw NotValidPassword("Minimum characters: 6.")
            else throw NotValidPassword("Both passwords must be equals.")
        else if(name.isBlank()) throw NotValidName("Name cant be blank.")
        else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val user = hashMapOf(
                    "email" to email,
                    "name" to name,
                    "image" to "gs://instalaciones-deportivas-ddb39.appspot.com/user.png",
                    "date" to "",
                    "phone" to "",
                    "puntuacion" to binding.spinnerPuntuacion.selectedItem.toString(),
                    "peticiones" to listOf<String>(),
                    "amigos" to listOf<String>()
                )
                database.collection("users").add(user).addOnSuccessListener {
                    Toast.makeText(applicationContext, "User registered.", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    loadSignIn()
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Email already in use.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkPassword(password: String): Boolean = password.isNotBlank() &&
                                                           password.length >= 6 &&
                                                           password == binding.inputPasswordRepeatSignUp.text.toString()

    private fun checkEmail(email: String): Boolean = email.isNotBlank() &&
                                                     android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun loadSignIn() {
        val intent = Intent(this, SigninActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }

}