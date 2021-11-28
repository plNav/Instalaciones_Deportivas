package martinez.alvaro.instalacionesdeportivas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        user = auth.currentUser

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.btnLogout -> logout()
            R.id.btnProfile -> profile()
            R.id.btnRanking -> ranking()
            R.id.btnPlay -> play()
            R.id.btnFriends -> friends()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun friends(): Boolean {
        val intent = Intent(this, FriendsActivity::class.java)
        this.startActivity(intent)
        return true
    }

    private fun play(): Boolean {
        val intent = Intent(this, PlayActivity::class.java)
        this.startActivity(intent)
        return true
    }

    private fun ranking(): Boolean {
        val intent = Intent(this, RankingActivity::class.java)
        this.startActivity(intent)
        return true
    }

    private fun profile(): Boolean {
        val intent = Intent(this, ProfileActivity::class.java)
        this.startActivity(intent)
        return true
    }

    private fun logout(): Boolean {
        auth.signOut()
        val intent = Intent(this, SigninActivity::class.java)
        this.startActivity(intent)
        this.finish()
        return true
    }

}