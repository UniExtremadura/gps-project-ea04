package es.unex.nbafantasy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.FragmentEditarBinding
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding
import es.unex.nbafantasy.databinding.FragmentPantJuegoBinding
import kotlinx.coroutines.launch


class EditarFragment : Fragment() {
    private var _todosJugadores: List<Jugador> = emptyList()
    private var _datas: MutableList<Jugador> = mutableListOf()
    private var _JugadoresEnEquipo: MutableList<Jugador> = mutableListOf()
    private lateinit var listener: OnEditarJugadorClickListener
    private lateinit var db: BD
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
            //Inicializacion BD
            db= BD.getInstance(requireContext())!!

        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        lifecycleScope.launch {
            if (_datas.isEmpty()) {
                binding.spinnerEditar.visibility = View.VISIBLE
                try {
                    _todosJugadores = db?.jugadorDao()?.getAll() ?: emptyList()//todos los jugadores

                    val usuario = (requireActivity() as? MainActivity)?.getUsuario()
                    Idusuario = usuario?.usuarioId?:0

                    listaUsuarioJugador = db?.usuarioJugadorDao()?.getJugadorByUsuario(Idusuario) ?: emptyList()

                    //guarda en datas todos los usuarios que
                    for(jug in listaUsuarioJugador){
                        for(todos in _todosJugadores) {
                            if(jug.jugadorId == todos.jugadorId) {
                                if (!_datas.contains(todos)) {
                                    // Insertar "todos" en la lista _datas
                                    _datas.add(todos)
                                }
                            }
                        }
                    }

                    /*for(jug in listaUsuarioEquipo){
                        for(todos in _todosJugadores) {
                            if(jug.jugadorId == todos.jugadorId) {
                                if (!_JugadoresEnEquipo.contains(todos)) {
                                    // Insertar "todos" en la lista _datas
                                    _JugadoresEnEquipo.add(todos)
                                }
                            }
                        }
                    }*/

                    adapter.updateData(_datas,Idusuario)

                } catch (error: APIError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                } finally {
                    binding.spinnerEditar.visibility = View.GONE
                }
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