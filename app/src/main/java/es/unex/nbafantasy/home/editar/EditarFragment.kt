package es.unex.nbafantasy.home.editar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.data.UsuarioJugadorRepository
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.data.JugadorEquipoRepository
import es.unex.nbafantasy.databinding.FragmentEditarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class EditarFragment : Fragment(), EditarAdapter.OnFavoriteButtonClickListener {
    private var _datas: MutableList<Jugador> = mutableListOf()
    private lateinit var listener: OnEditarJugadorClickListener
    private val EditarviewModel: EditarViewModel by viewModels { EditarViewModel.Factory }
    private var Idusuario: Long = 0
    private var jugadoresFavoritos: MutableMap<Long, Boolean> = mutableMapOf()

    interface OnEditarJugadorClickListener {
        fun onEditClick(data: Jugador, estrella: Boolean)
    }

    private lateinit var adapter: EditarAdapter
    private var _binding: FragmentEditarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnEditarJugadorClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        val usuario = ((requireActivity() as? MainActivity)?.getUsuario())
        EditarviewModel.usuario = usuario

        EditarviewModel.spinner.observe(viewLifecycleOwner) { jugador ->
            binding.spinnerEditar.visibility = if (jugador) View.VISIBLE else View.GONE
        } // Show a Toast whenever the [toast] is updated a non-null value
        EditarviewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                EditarviewModel.onToastShown()
            }
        }
        subscribeEditarJugadores()
    }


    private fun subscribeEditarJugadores() {
        // Primero, obtener los jugadores favoritos actuales del usuario
        EditarviewModel.getJugadoresEnEquipoDeUsuario().observe(viewLifecycleOwner) { jugadoresEquipo ->
        // Crear un conjunto de IDs de jugadores favoritos
            val favoritosIds = jugadoresEquipo.mapNotNull { it.jugadorId }.toSet()

            // Actualizar el mapa de jugadores favoritos
            jugadoresFavoritos.clear()
            favoritosIds.forEach { id ->
                jugadoresFavoritos[id] = true
            }

            // Luego, obtener todos los jugadores y actualizar el adaptador
            EditarviewModel.getJugadoresDeUsuario().observe(viewLifecycleOwner) { jugadores ->

                adapter.updateData(jugadores, EditarviewModel.usuario!!.usuarioId!!, jugadoresFavoritos)
            }
        }
    }

    private fun setUpRecyclerView() {
        // Asegúrate de que jugadoresFavoritos esté inicializado
        jugadoresFavoritos = mutableMapOf() // o con valores predeterminados si los tienes

        adapter = EditarAdapter(
            DataS = _datas,
            usuario = Idusuario,
            jugadoresFavoritos = jugadoresFavoritos,
            onClick = { jugador, estrella ->
                listener.onEditClick(jugador, estrella)
            },
            onLongClick = {
                Toast.makeText(context, "long click on: " + it.apellido, Toast.LENGTH_SHORT).show()
            },
            onFavoriteButtonClick = { jugador, position ->
                onFavoriteButtonClick(jugador, position)
            },
            lifecycleOwner = viewLifecycleOwner
        )

        with(binding) {
            rvEditList.layoutManager = LinearLayoutManager(context)
            rvEditList.adapter = adapter
        }

        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }

    override fun onFavoriteButtonClick(jugador: Jugador, position: Int) {
        val isFavorite = jugadoresFavoritos[jugador.jugadorId] ?: false
        val favoritosActuales = jugadoresFavoritos.count { it.value }

        if (isFavorite) {
            // Si el jugador ya es favorito, eliminarlo de los favoritos.
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    EditarviewModel.eliminarViewModel(jugador.jugadorId ?: 0)
                    jugadoresFavoritos[jugador.jugadorId ?: 0] = false
                    Toast.makeText(context, "Jugador eliminado del equipo", Toast.LENGTH_SHORT).show()
                    adapter.updateItem(jugador, position, false)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al eliminar jugador", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (favoritosActuales < 3) {
            // Si el jugador no es favorito y hay menos de 3 favoritos, añadirlo.
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    EditarviewModel.insertarViewModel(jugador.jugadorId ?: 0)
                    jugadoresFavoritos[jugador.jugadorId ?: 0] = true
                    Toast.makeText(context, "Jugador añadido al equipo", Toast.LENGTH_SHORT).show()
                    adapter.updateItem(jugador, position, true)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al añadir jugador", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Si el jugador no es favorito y ya hay 3 favoritos, no hacer nada.
            Toast.makeText(context, "No se puede añadir: el equipo ya tiene 3 jugadores", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}