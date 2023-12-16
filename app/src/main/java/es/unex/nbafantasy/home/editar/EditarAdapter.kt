package es.unex.nbafantasy.home.editar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import es.unex.nbafantasy.data.JugadorEquipoRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.PlayerItemEditarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarAdapter(
    private var DataS: List<Jugador>,
    private var usuario: Long,
    private var jugadoresFavoritos: Map<Long, Boolean>,
    private val onClick: (Jugador, Boolean) -> Unit,
    private val onLongClick: (Jugador) -> Unit,
    private val onFavoriteButtonClick: (Jugador, Int) -> Unit,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<EditarAdapter.ShowViewHolder>() {
    interface OnFavoriteButtonClickListener {
        fun onFavoriteButtonClick(jugador: Jugador, position: Int)
    }
    inner class ShowViewHolder(
        private val binding: PlayerItemEditarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(jugador: Jugador,usuarioid:Long, position: Int) {
            with(binding) {
                playersNameEdit.text = "${jugador.nombre} ${jugador.apellido}"
                playersTeamEdit.text = "Equipo: ${jugador.equipo}"
                playersPositionEdit.text = "Posicion: ${jugador.posicion}"
                playersStatsEdit.text = "Estadisticas: ${jugador.mediaGeneralPartido}"

                val isFavorite = jugadoresFavoritos[jugador.jugadorId] ?: false
                btnFavorite.isSelected = isFavorite

                btnFavorite.setOnClickListener {
                    onFavoriteButtonClick(jugador, position)
                }

                clEditar.setOnClickListener {
                    onClick(jugador, isFavorite)
                }
                clEditar.setOnLongClickListener {
                    onLongClick(jugador)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = PlayerItemEditarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding)
    }

    override fun getItemCount(): Int = DataS.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(DataS[position],usuario,position)
    }

    fun updateData(newData: List<Jugador>, newUsuario: Long, newFavoritos: Map<Long, Boolean>) {
        this.DataS = newData
        this.usuario = newUsuario
        this.jugadoresFavoritos = newFavoritos
        notifyDataSetChanged()
    }

    fun updateItem(jugador: Jugador, position: Int, isFavorite: Boolean) {
        jugadoresFavoritos = jugadoresFavoritos.toMutableMap().apply {
            put(jugador.jugadorId ?: 0L, isFavorite)
        }
        notifyItemChanged(position)
    }
}

