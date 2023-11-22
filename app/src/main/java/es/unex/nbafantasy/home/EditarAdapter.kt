package es.unex.nbafantasy.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.PlayerItemEditarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class EditarAdapter (
    private var DataS: List<Jugador>,
    private var usuario: Long,
    private val onClick: (show: Jugador,estrella: Boolean) -> Unit,
    private val onLongClick: (title: Jugador) -> Unit,
    private val lifecycleOwner: LifecycleOwner
) :RecyclerView.Adapter<EditarAdapter.ShowViewHolder>() {
    private val jugadoresSeleccionados = mutableListOf<Jugador>()
    private lateinit var listaUsuarioEquipo: List<JugadorEquipo>
    private lateinit var db: BD
        inner class ShowViewHolder(
            private val binding: PlayerItemEditarBinding,
            private val onClick: (show: Jugador,estrella: Boolean) -> Unit,
            private val onLongClick: (title: Jugador) -> Unit,
        ) : RecyclerView.ViewHolder(binding.root) {
            private var isFavorite: Boolean = false
            fun bind(nbadata: Jugador,usuario:Long, adritotalItems: Int) {
                //Log.d("Player ID", "Player ID: ${nbadata.firstName}")
                with(binding) {
                    Log.d("Player nombre", "Player nombre: ${nbadata.nombre}")
                    playersNameEdit.text = "${nbadata.nombre}" + " " + "${nbadata.apellido}"
                    playersTeamEdit.text = "Equipo: " + "${nbadata.equipo}"
                    playersPositionEdit.text = "Posicion: " + "${nbadata.posicion}"
                    playersStatsEdit.text = "Estadisticas: " + "${nbadata.mediaGeneralPartido}"

                    lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        listaUsuarioEquipo =
                            db?.jugadorEquipoDao()?.getJugadorByUsuario(usuario) ?: emptyList()
                        for (equipo in listaUsuarioEquipo) {
                            if (equipo.jugadorId == nbadata.jugadorId) {
                                isFavorite = true
                                btnFavorite.isSelected = isFavorite
                                if (jugadoresSeleccionados.size < 3) {
                                    jugadoresSeleccionados.add(nbadata)
                                    Log.d("equipo ID", "INSERTADO ID: ${nbadata.jugadorId}")
                                } else {
                                    if (jugadoresSeleccionados.size == 3) {
                                        for (enlista in jugadoresSeleccionados) {
                                            Log.d("equipo ID", "equipo ID: ${equipo.jugadorId}")
                                            Log.d("enlista ID", "enlista ID: ${enlista.jugadorId}")
                                            if (equipo.jugadorId != enlista.jugadorId) {
                                                jugadoresSeleccionados.add(nbadata)
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        binding.btnFavorite.setOnClickListener {
                            if(jugadoresSeleccionados.size == 3 && !isFavorite) {
                                Toast.makeText(itemView.context, "Equipo completo", Toast.LENGTH_SHORT).show()
                            }else{
                                isFavorite = !isFavorite
                            }
                            if (isFavorite) {
                                // Verificar si ya hay 3 jugadores seleccionados
                                if (jugadoresSeleccionados.size < 3) {
                                    Log.d("Player nombre", "jugador añadido: ${nbadata.nombre}")
                                    // Agregar el jugador a la lista de seleccionados
                                    lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                        insertarUsuarioEquipo(usuario, nbadata.jugadorId ?: 0)
                                    }
                                    jugadoresSeleccionados.add(nbadata)
                                    Log.d("Player nombre", "tamaño: ${jugadoresSeleccionados.size}")
                                    updateFavoriteButtonState()
                                    if (jugadoresSeleccionados.size == 3) {
                                        // Mostrar mensaje de "equipo completo"
                                        Toast.makeText(itemView.context, "Equipo completo", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                // Remover el jugador de la lista de seleccionados si se deselecciona
                                Log.d("Player nombre", "jugador eliminado: ${nbadata.nombre}")
                                lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                    EliminarUsuarioEquipo(usuario, nbadata.jugadorId ?: 0)
                                }
                                jugadoresSeleccionados.remove(nbadata)
                                Log.d("Player nombre", "tamaño: ${jugadoresSeleccionados.size}")
                                // Llamar a la función independientemente del tamaño de la lista si isFavorite es false
                                updateFavoriteButtonState()
                            }
                        }
                    }

                    clEditar.setOnClickListener {
                        onClick(nbadata,isFavorite)
                    }
                    clEditar.setOnLongClickListener {
                        onLongClick(nbadata)
                        true
                    }
                }
            }
            private fun updateFavoriteButtonState() {
                Log.d("FavoriteButtonState", "isFavorite: $isFavorite")
                binding.btnFavorite.isSelected = isFavorite
                //if (isFavorite) {
                //} else {
                //}
            }
            private suspend fun insertarUsuarioEquipo(usuario:Long,jugadorId:Long){
                db.jugadorEquipoDao().insertar(JugadorEquipo(usuario,jugadorId))
            }
            private suspend fun EliminarUsuarioEquipo(usuario:Long,jugadorId:Long){
                db.jugadorEquipoDao().Eliminar(usuario,jugadorId)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                ShowViewHolder {
            //Inicializacion BD
            db= BD.getInstance(parent.context)!!
            val binding = PlayerItemEditarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ShowViewHolder(binding, onClick, onLongClick)
        }

        override fun getItemCount() = DataS.size

        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
                holder.bind(DataS[position],usuario, DataS.size)
        }
        fun updateData(DataS: List<Jugador>, usuarioid :Long) {
            this.DataS = DataS
            this.usuario = usuarioid
            notifyDataSetChanged()
        }
    }