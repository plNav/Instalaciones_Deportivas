package martinez.alvaro.instalacionesdeportivas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener { login(inputEmail.editableText.toString(), inputPassword.editableText.toString()) }
        txtCreateAccount.setOnClickListener { createAccount() }
        txtForgotPassword.setOnClickListener { restorePassword() }
    }

    private fun login(email:String?, password:String?) {
        // TODO

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