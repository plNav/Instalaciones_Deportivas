package martinez.alvaro.instalacionesdeportivas

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
        /* EMAIL FORMAT VALIDATION */
        if(!isValidEmail(email)) {
            txtEmailError.visibility = View.VISIBLE
            return
        }
        else txtEmailError.visibility = View.GONE

        /* CHECK IF USER IS REGISTERED ON DATABASE */
        // IF NOT
            // ASK USER IF WANT TO REGISTER
            // IF YES GO TO REGISTER PAGE
            // ELSE DONT DO ANYTHING
        // ELSE GO TO MAIN LAYOUT

    }

    private fun createAccount(){
        // TODO
    }

    private fun restorePassword() {
        // TODO
    }

    private fun isValidEmail(email:String?):Boolean = !(email === null) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

}