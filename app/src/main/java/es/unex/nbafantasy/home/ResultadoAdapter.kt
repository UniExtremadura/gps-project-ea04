package es.unex.nbafantasy.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.databinding.PlayerItemBinding
import es.unex.nbafantasy.databinding.ResultadoItemBinding
import kotlin.math.log

class ResultadoAdapter (
    private var DataS: List<ResultadoPartido>,
    private val onClick: (show: ResultadoPartido) -> Unit,
    private val onLongClick: (title: ResultadoPartido) -> Unit
    ) :RecyclerView.Adapter<ResultadoAdapter.ShowViewHolder>() {
        class ShowViewHolder(
            private val binding: ResultadoItemBinding,
            private val context: Context,
            private val onClick: (show: ResultadoPartido) -> Unit,
            private val onLongClick: (title: ResultadoPartido) -> Unit,
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(result: ResultadoPartido, totalItems: Int) {
                with(binding) {
                    resultadoPartido.text = "Resultado: " + "${result.estadoResultado}"
                    misPuntos.text = "Mis puntos: " + "${result.misPuntos}"
                    puntosRival.text = "Puntos del rival: " + "${result.puntosRivales}"

                    if(result.estadoResultado.equals("Victoria")){
                        val verde = ContextCompat.getColor(context, R.color.Lightgreen)
                        clTarget.setBackgroundColor(verde)
                    }else{
                        val azul = ContextCompat.getColor(context, R.color.lavenderblue)
                        clTarget.setBackgroundColor(azul)
                    }

                    clItem.setOnClickListener {
                        onClick(result)
                    }
                    clItem.setOnLongClickListener {
                        onLongClick(result)
                        true
                    }
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                ShowViewHolder {
            val binding =ResultadoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ShowViewHolder(binding,parent.context, onClick, onLongClick)
        }

        override fun getItemCount() = DataS.size

        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
            val nbadata = DataS[position]
            holder.bind(nbadata, DataS.size)
        }
        fun updateData(DataS: List<ResultadoPartido>) {
            this.DataS = DataS
            notifyDataSetChanged()
        }
}