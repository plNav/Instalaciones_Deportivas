package martinez.alvaro.instalacionesdeportivas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener { Login(inputEmail.editableText.toString(), inputPassword.editableText.toString()) }
        txtCreateAccount.setOnClickListener { CreateAccount() }
        txtForgotPassword.setOnClickListener { RestorePassword() }
    }

    private fun Login(email:String, password:String) {
        // TODO
        println("Email: $email, Password: $password")
    }

    private fun CreateAccount(){
        // TODO
    }

    private fun RestorePassword() {
        // TODO
    }

}