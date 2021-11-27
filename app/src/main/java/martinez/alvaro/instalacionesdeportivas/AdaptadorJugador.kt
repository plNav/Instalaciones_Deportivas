package martinez.alvaro.instalacionesdeportivas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdaptadorJugador(var listaJugadores: List<Jugador>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vista = inflater.inflate(R.layout.recycler_jugador_ranking, parent, false)
        return JugadorViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as JugadorViewHolder).bind(listaJugadores[position])
    }

    override fun getItemCount(): Int {
        return listaJugadores.size
    }
}