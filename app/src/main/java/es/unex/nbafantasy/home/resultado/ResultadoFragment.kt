package es.unex.nbafantasy.home.resultado

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
import es.unex.nbafantasy.databinding.FragmentResultadoBinding

class ResultadoFragment : Fragment() {
    private var _binding: FragmentResultadoBinding? = null
    private lateinit var adapter: ResultadoAdapter
    private val binding get() = _binding!!

    private val viewModel: ResultadoViewModel by viewModels { ResultadoViewModel.Factory }
    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultadoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa LiveData en onViewCreated
        viewModel.spinner.observe(viewLifecycleOwner) { show ->
            binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
        }

        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        mainViewModel.usuario.observe(viewLifecycleOwner) { usuario ->
            viewModel.usuario = usuario
            setUpRecyclerView()
            subscribeUiResultados(adapter)
        }
    }

    private fun subscribeUiResultados(adapter: ResultadoAdapter) {
        viewModel.getResultado().observe(viewLifecycleOwner) { resultados ->
            adapter.updateData(resultados)
        }
    }

    private fun setUpRecyclerView() {
        adapter = ResultadoAdapter(
            DataS = emptyList(),
            onClick = {
                mainViewModel.onResultClick(it)
            },
            onLongClick = {
                Toast.makeText(context, "long click on: " + it.estadoResultado, Toast.LENGTH_SHORT).show()
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
        _binding = null // evitar pérdida de memoria
    }
}