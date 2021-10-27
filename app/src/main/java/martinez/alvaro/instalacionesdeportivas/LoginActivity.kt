package martinez.alvaro.instalacionesdeportivas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.btnLogin.setOnClickListener { login(binding.inputEmail.text.toString(), binding.inputPassword.text.toString()) }
        binding.txtCreateAccount.setOnClickListener { createAccount() }
        binding.txtForgotPassword.setOnClickListener { restorePassword() }
    }

    private fun login(email:String?, password:String?) {

        /* CHECK IF USER IS REGISTERED ON DATABASE */

        if(email.isNullOrBlank() || password.isNullOrBlank()) return

        auth.signInWithEmailAndPassword(email, password).
            addOnCompleteListener(this) {
                if(it.isSuccessful) loadMainLayout()
                else Toast.makeText(applicationContext, "User or password is incorrect.", Toast.LENGTH_SHORT).show()
            }

        /* CHECK IF USER IS REGISTERED ON DATABASE */

    }

    private fun loadMainLayout() {
        TODO("Not yet implemented")
    }

    private fun createAccount(){

        /* LOAD CREATE ACCOUNT LAYOUT */

            // private fun isValidEmail(email:String?):Boolean = !(email === null) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        /* LOAD CREATE ACCOUNT LAYOUT */

    }

    private fun restorePassword() {
        // TODO
    }

}