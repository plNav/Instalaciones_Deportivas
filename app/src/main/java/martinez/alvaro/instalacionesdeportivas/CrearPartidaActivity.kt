package martinez.alvaro.instalacionesdeportivas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import martinez.alvaro.instalacionesdeportivas.databinding.ActivityCrearPartidaBinding
import java.util.*
import kotlin.math.min

class CrearPartidaActivity : AppCompatActivity() {
    lateinit var binding: ActivityCrearPartidaBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val date = Date()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearPartidaBinding.inflate(layoutInflater)
        auth = Firebase.auth
        database = Firebase.firestore
        setContentView(binding.root)

        binding.inputFechaPartida.text = getDate(date)
        binding.inputHoraPartida.text = getHour(date)

        binding.inputFechaPartida.setOnClickListener { cambiarFecha() }
        binding.inputHoraPartida.setOnClickListener { cambiarHora() }
        binding.btnCancelar.setOnClickListener { this.finish() }
        binding.bntAceptar.setOnClickListener { crearPartida() }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun cambiarHora() {
        TimePickerDialog(this,
            { view, hourOfDay, minute ->
                binding.inputHoraPartida.text = "${if(hourOfDay < 10) "0${hourOfDay}" else hourOfDay}:${if(minute < 10) "0${minute}" else minute}"
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun cambiarFecha() {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            binding.inputFechaPartida.text = "${if(dayOfMonth < 10) "0${dayOfMonth}" else dayOfMonth}/${if(month < 10) "0${month}" else month}/${year}"
        }
        datePickerDialog.show()
    }

    private fun getHour(date: Date): String = "${if(date.hours < 10) "0${date.hours}" else date.hours}:${if(date.minutes < 10) "0${date.minutes}" else date.minutes}"
    private fun getDate(date: Date): String = "${if(date.day < 10) "0${date.day}" else date.day}/${if(date.month < 10) "0${date.month}" else date.month}/2021"


    private fun crearPartida() {

        val valoresFecha = binding.inputFechaPartida.text.split("/").map { it.toInt() }
        val valoresHora = binding.inputHoraPartida.text.split(":").map { it.toInt() }

        val actualMilis = date.time
        val dateMilis = Date(valoresFecha[2], valoresFecha[1], valoresFecha[0], valoresHora[0], valoresHora[1]).time

        if(dateMilis - actualMilis < 0) {
            setError(binding.inputHoraPartida, "Hora no valida")
            setError(binding.inputFechaPartida, "Fecha no valida")
            return
        }

        database.collection("users").whereEqualTo("email", auth.currentUser?.email).get()
        .addOnCompleteListener {
            val doc = it.result?.documents?.get(0)
            val image = doc?.get("image").toString()
            val email = doc?.get("email").toString()
            val partida = hashMapOf(
                "fecha" to binding.inputFechaPartida.text.toString(),
                "hora" to binding.inputHoraPartida.text.toString(),
                "jugadores" to listOf(mapOf("email" to email, "image" to image))
            )
            database.collection("partidas").add(partida).addOnCompleteListener {
                Toast.makeText(applicationContext, "Partida creada", Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }
    }

    private fun setError(view: TextView, message: String) {
        view.error = message
    }
}
