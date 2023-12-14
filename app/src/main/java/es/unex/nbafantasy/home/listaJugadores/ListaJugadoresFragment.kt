package es.unex.nbafantasy.home.listaJugadores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListaJugadoresFragment : Fragment() {
    private var _datas: List<Jugador> = emptyList()
    private lateinit var listener: OnJugadorClickListener
    private lateinit var jugadorRepository: JugadorRepository

    interface OnJugadorClickListener {
        fun onShowClick(data: Jugador)
    }

    private var _binding: FragmentListaJugadoresBinding? = null
    private val binding get()=_binding!!
    private lateinit var adapter: ListaJugadoresAdapter

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnJugadorClickListener) {
            listener = context


        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaJugadoresBinding.inflate(inflater,container, false)
        return binding.root
   }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        val appContainer = (this.activity?.application as NBAFantasyApplication).appContainer
        jugadorRepository = appContainer.repositoryJugador

        subscribeListaJugadores(adapter)
        launchDataLoad { jugadorRepository }
    }

    private fun subscribeListaJugadores(adapter: ListaJugadoresAdapter) {
        jugadorRepository.jugadores.observe(viewLifecycleOwner) { jugadores ->
            adapter.updateData(jugadores)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return lifecycleScope.launch {
            try {
                binding.spinner.visibility = View.VISIBLE
                block()
            } catch (error: APIError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } finally {
                binding.spinner.visibility = View.GONE
            }
        }
    }

    private fun setUpRecyclerView() {
        adapter = ListaJugadoresAdapter(DataS = _datas, onClick = {
            listener.onShowClick(it)
        },
            onLongClick = {
                Toast.makeText(context, "long click on: "+it.apellido, Toast.LENGTH_SHORT).show()
            }
        )
        with(binding) {
            rvPlayersList.layoutManager = LinearLayoutManager(context)
            rvPlayersList.adapter = adapter

        }

        android.util.Log.d("ListaJugadoresFragment", "setUpRecyclerView")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}