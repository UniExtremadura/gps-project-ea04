package es.unex.nbafantasy.home.listaJugadores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.nbafantasy.MainViewModel

import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding


class ListaJugadoresFragment : Fragment() {
    private val viewModel: ListaJugadoresViewModel by viewModels { ListaJugadoresViewModel.Factory }
    private val mainviewModel: MainViewModel by activityViewModels()


    private var _binding: FragmentListaJugadoresBinding? = null
    private val binding get()=_binding!!
    private lateinit var adapter: ListaJugadoresAdapter

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

        viewModel.spinner.observe(viewLifecycleOwner) { jugador ->
            binding.spinner.visibility = if (jugador) View.VISIBLE else View.GONE
        } // Show a Toast whenever the [toast] is updated a non-null value
        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
        subscribeListaJugadores(adapter)
    }

    private fun subscribeListaJugadores(adapter: ListaJugadoresAdapter) {
        viewModel.listaJugadores.observe(viewLifecycleOwner) { jugadores ->
            adapter.updateData(jugadores)
        }
    }


    private fun setUpRecyclerView() {
        adapter = ListaJugadoresAdapter(DataS = emptyList(), onClick = {
            mainviewModel.onShowClick(it)
            //listener.onShowClick(it)
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