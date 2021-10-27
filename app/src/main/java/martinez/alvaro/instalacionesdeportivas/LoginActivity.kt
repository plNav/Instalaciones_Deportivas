package martinez.alvaro.instalacionesdeportivas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener { login(binding.inputEmail.text.toString(), binding.inputPassword.text.toString()) }
        binding.txtCreateAccount.setOnClickListener { createAccount() }
        binding.txtForgotPassword.setOnClickListener { restorePassword() }
    }

    private fun login(email:String?, password:String?) {
        // TODO

        Toast.makeText(applicationContext, "Email: $email, Password: $password", Toast.LENGTH_SHORT).show()

        /* CHECK IF USER IS REGISTERED ON DATABASE */

        // IF NOT
          //
            // ASK USER IF WANT TO REGISTER
            // IF YES GO TO REGISTER PAGE
            // ELSE DONT DO ANYTHING
          //
        // ELSE
          //
            // LOAD MAIN LAYOUT
          //

        /* CHECK IF USER IS REGISTERED ON DATABASE */
    }

    private fun createAccount(){
        // TODO

        /* LOAD CREATE ACCOUNT LAYOUT */

            // private fun isValidEmail(email:String?):Boolean = !(email === null) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        /* LOAD CREATE ACCOUNT LAYOUT */
    }

    private fun restorePassword() {
        // TODO
    }

}