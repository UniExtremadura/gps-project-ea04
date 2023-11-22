package es.unex.nbafantasy.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.nbafantasy.Data.api.Data
import es.unex.nbafantasy.Data.model.NBAData
import es.unex.nbafantasy.Data.model.NBASeasonData
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding
import es.unex.nbafantasy.databinding.PlayerItemBinding
class ListaJugadoresAdapter (
    private var DataS: List<Jugador>,
    private val onClick: (show: Jugador) -> Unit,
    private val onLongClick: (title: Jugador) -> Unit
    ) :RecyclerView.Adapter<ListaJugadoresAdapter.ShowViewHolder>() {
    class ShowViewHolder(
        private val binding: PlayerItemBinding,
        private val onClick: (show: Jugador) -> Unit,
        private val onLongClick: (title: Jugador) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nbadata: Jugador, totalItems: Int) {
            //Log.d("Player ID", "Player ID: ${nbadata.firstName}")
            with(binding) {
                Log.d("Player nombre", "Player nombre: ${nbadata.nombre}")
                playersName.text = "${nbadata.nombre}" + " " + "${nbadata.apellido}"
                playersTeam.text = "Equipo: " + "${nbadata.equipo}"
                playersPosition.text = "Posicion: " + "${nbadata.posicion}"
                playersStats.text = "Estadisticas: " + "${nbadata.mediaGeneralPartido}"

                clItem.setOnClickListener {
                    onClick(nbadata)
                }
                clItem.setOnLongClickListener {
                    onLongClick(nbadata)
                    true
                }
            }
        }
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                ShowViewHolder {
            val binding = PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ShowViewHolder(binding, onClick, onLongClick)
        }

        override fun getItemCount() = DataS.size

        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
            val nbadata = DataS[position]
            holder.bind(nbadata, DataS.size)
        }
    fun updateData(DataS: List<Jugador>) {
        this.DataS = DataS
        notifyDataSetChanged()
    }
    }
