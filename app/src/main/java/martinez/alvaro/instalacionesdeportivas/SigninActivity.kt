package martinez.alvaro.instalacionesdeportivas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import martinez.alvaro.instalacionesdeportivas.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        if(auth.currentUser != null) loadMainLayout()

        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignIn.setOnClickListener { login(binding.inputEmail.text.toString(), binding.inputPassword.text.toString()) }
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
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    }

    private fun createAccount(){
        val intent = Intent(this, SignupActivity::class.java)
        this.startActivity(intent)
    }

    private fun restorePassword() {
        // TODO
    }

}