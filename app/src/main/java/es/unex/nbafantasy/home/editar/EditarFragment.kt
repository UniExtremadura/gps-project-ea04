package es.unex.nbafantasy.home.editar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.Data.UsuarioJugadorRepository
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.databinding.FragmentEditarBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class EditarFragment : Fragment() {
    private var _todosJugadores: List<Jugador> = emptyList()
    private var _datas: MutableList<Jugador> = mutableListOf()
    private lateinit var jugadorRepository: JugadorRepository
    private lateinit var usuarioJugadorRepository: UsuarioJugadorRepository
    private lateinit var listener: OnEditarJugadorClickListener
    private var Idusuario: Long = 0
    interface OnEditarJugadorClickListener {
        fun onEditClick(data: Jugador, estrella: Boolean)
    }
    private lateinit var adapter: EditarAdapter
    private var _binding: FragmentEditarBinding? = null
    private val binding get()=_binding!!
    private lateinit var listaUsuarioJugador: List<UsuarioJugador>
    private lateinit var listaUsuarioEquipo: List<JugadorEquipo>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentEditarBinding.inflate(layoutInflater,container, false)
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

        val appContainer = (this.activity?.application as NBAFantasyApplication).appContainer
        jugadorRepository = appContainer.repositoryJugador
        usuarioJugadorRepository = appContainer.repositoryUsuarioJugador

        val usuarioId = ((requireActivity() as? MainActivity)?.getUsuario())?.usuarioId?:0
        subscribeEditarJugadores(usuarioId)
        launchDataLoad { usuarioJugadorRepository }
    }
    private fun subscribeEditarJugadores(usuarioId: Long) {
        usuarioJugadorRepository.obtenerJugadoresDeUsuario(usuarioId)
            .observe(viewLifecycleOwner) { jugadores ->
                adapter.updateData(jugadores, usuarioId)
            }
    }
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return lifecycleScope.launch {
            try {
                binding.spinnerEditar.visibility = View.VISIBLE
                block()
            } catch (error: APIError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } finally {
                binding.spinnerEditar.visibility = View.GONE
            }
        }
    }
    private fun setUpRecyclerView() {
        adapter = EditarAdapter(DataS = _datas, usuario = Idusuario ,onClick = { Jugador,estrella ->
            listener.onEditClick(Jugador,estrella)
        },
            onLongClick = {
                Toast.makeText(context, "long click on: "+it.apellido, Toast.LENGTH_SHORT).show()
            },
            lifecycleOwner = viewLifecycleOwner
        )
        with(binding) {
            rvEditList.layoutManager = LinearLayoutManager(context)
            rvEditList.adapter = adapter

        }

        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}