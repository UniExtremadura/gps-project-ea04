package es.unex.nbafantasy.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.nbafantasy.Data.api.Data
import es.unex.nbafantasy.Data.model.NBAData
import es.unex.nbafantasy.Data.model.NBASeasonData
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding
import es.unex.nbafantasy.databinding.PlayerItemBinding
class ListaJugadoresAdapter (
        private var DataS: List<NBAData>,
        private var SeasonData: List<NBASeasonData>,
        private val onClick: (show: NBAData, showseason: NBASeasonData) -> Unit,
        private val onLongClick: (title: NBAData) -> Unit
    ) :RecyclerView.Adapter<ListaJugadoresAdapter.ShowViewHolder>() {
    class ShowViewHolder(
        private val binding: PlayerItemBinding,
        private val onClick: (show: NBAData, showseason: NBASeasonData) -> Unit,
        private val onLongClick: (title: NBAData) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nbadata: NBAData, totalItems: Int, seasonData: NBASeasonData) {
            //Log.d("Player ID", "Player ID: ${nbadata.firstName}")
            with(binding) {
                Log.d("Player nombre", "Player nombre: ${nbadata.firstName}")
                playersName.text = "${nbadata.firstName}" + " " + "${nbadata.lastName}"
                playersTeam.text = "Equipo: " + "${nbadata.team?.fullName}"
                playersPosition.text = "Posicion: " + "${nbadata.position}"
                playersStats.text = "Estadisticas: " + "${seasonData.pts}"

                clItem.setOnClickListener {
                    onClick(nbadata,seasonData)
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
            val seasonData = SeasonData[position]
            holder.bind(nbadata, DataS.size, seasonData)
        }
    fun updateData(DataS: List<NBAData>, SeasonS: List<NBASeasonData>) {
        this.DataS = DataS
        this.SeasonData = SeasonS
        notifyDataSetChanged()
    }
    }
