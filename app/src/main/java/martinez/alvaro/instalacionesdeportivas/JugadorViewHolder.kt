package martinez.alvaro.instalacionesdeportivas

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class JugadorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val nombre = itemView.findViewById(R.id.txtNombreRecycler) as TextView
    val puntuacion = itemView.findViewById(R.id.txtPuntuacionRecycler) as TextView
    val imagen = itemView.findViewById(R.id.imgUsuario) as ImageView
    fun bind(jugador: Jugador) {
        nombre.text = jugador.nombre
        puntuacion.text = jugador.puntuacion
        Glide.with(itemView).load(jugador.uri).into(imagen)
    }
}