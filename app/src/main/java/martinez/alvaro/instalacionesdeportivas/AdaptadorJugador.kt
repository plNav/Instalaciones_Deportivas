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

class AdaptadorAmigo(var listaAmigos: List<Amigo>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vista = inflater.inflate(R.layout.recycler_friends, parent, false)
        return AmigoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AmigoViewHolder).bind(listaAmigos[position])
    }

    override fun getItemCount(): Int {
        return listaAmigos.size
    }
}

class AdaptadorPartida(var listaPartidas: List<Partida>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vista = inflater.inflate(R.layout.recycler_partida, parent, false)
        return PartidaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartidaViewHolder).bind(listaPartidas[position])
    }

    override fun getItemCount(): Int {
        return listaPartidas.size
    }

}

class AdaptadorUsuario(var listaUsuarios: List<Usuario>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vista = inflater.inflate(R.layout.recycler_usuario, parent, false)
        return UsuarioViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UsuarioViewHolder).bind(listaUsuarios[position])
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

}

class AdaptadorSolicitud(var listaUsuarios: List<Usuario>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vista = inflater.inflate(R.layout.recycler_solicitud, parent, false)
        return SolicitudViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SolicitudViewHolder).bind(listaUsuarios[position])
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

}