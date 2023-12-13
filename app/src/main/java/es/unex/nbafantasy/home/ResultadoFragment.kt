package es.unex.nbafantasy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.nbafantasy.Data.ResultadoPartidoRepository
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.api.getNetworkService
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding
import es.unex.nbafantasy.databinding.FragmentResultadoBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ResultadoFragment : Fragment() {
    private var _binding:FragmentResultadoBinding?=null
    private var _datas: List<ResultadoPartido> = emptyList()
    private var Idusuario: Long = 0
    private lateinit var listener: OnResultadoClickListener
    private lateinit var db: BD
    private lateinit var adapter: ResultadoAdapter
    private lateinit var repositoryResultadoPartido: ResultadoPartidoRepository
    private val binding get()=_binding!!

    interface OnResultadoClickListener {
        fun onResultClick(data: ResultadoPartido)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentResultadoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnResultadoClickListener) {
            listener = context
            //Inicializacion BD
            db= BD.getInstance(context)!!
            repositoryResultadoPartido = ResultadoPartidoRepository.getInstance(db.resultadoPartidoDao())

        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        subscribeUiResultados(adapter)
        launchDataLoad { repositoryResultadoPartido }
    }
    private fun subscribeUiResultados(adapter: ResultadoAdapter) {
        repositoryResultadoPartido.resultados.observe(viewLifecycleOwner) { resultados ->
            adapter.updateData(resultados)
        }
    }
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return lifecycleScope.launch {
            try { binding.spinner.visibility = View.VISIBLE
                block()
            } catch (error: APIError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } finally {
                binding.spinner.visibility = View.GONE
            }
        }
    }
    private fun setUpRecyclerView() {
        adapter = ResultadoAdapter(DataS = _datas, onClick = {
            listener.onResultClick(it)
        },
            onLongClick = {
                Toast.makeText(context, "long click on: "+it.estadoResultado, Toast.LENGTH_SHORT).show()
            }
        )
        with(binding) {
            rvResultList.layoutManager = LinearLayoutManager(context)
            rvResultList.adapter = adapter

        }

        android.util.Log.d("ResultadoFragment", "setUpRecyclerView")

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}