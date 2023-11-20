package es.unex.nbafantasy.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.example.SeasonData
import es.unex.nbafantasy.Data.api.Data
import es.unex.nbafantasy.Data.model.NBAData
import es.unex.nbafantasy.Data.model.NBASeasonData
import es.unex.nbafantasy.Data.toNBAData

import es.unex.nbafantasy.Data.toSeasonAverages
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.api.getNetworkService
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding
import kotlinx.coroutines.launch

class ListaJugadoresFragment : Fragment() {
    private var _datas: List<NBAData> = emptyList()
    private var _seasondatas: List<NBASeasonData> = emptyList()
    private lateinit var listener: OnShowClickListener
    interface OnShowClickListener {
        fun onShowClick(data: NBAData, seasondata: NBASeasonData)
    }

    private var _binding: FragmentListaJugadoresBinding? = null
    private val binding get()=_binding!!
    private lateinit var adapter: ListaJugadoresAdapter

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaJugadoresBinding.inflate(layoutInflater,container, false)
        return binding.root
   }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        lifecycleScope.launch {
            if (_datas.isEmpty() && _seasondatas.isEmpty()) {
                binding.spinner.visibility = View.VISIBLE
                try {
                    _datas = emptyList()
                    _seasondatas = emptyList()

                    adapter.updateData(_datas,_seasondatas)

                } catch (error: APIError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                } finally {
                    binding.spinner.visibility = View.GONE
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        adapter = ListaJugadoresAdapter(DataS = _datas, SeasonData = _seasondatas, onClick = { data, seasonData ->
            listener.onShowClick(data, seasonData)
        },
            onLongClick = {
                Toast.makeText(context, "long click on: "+it.lastName, Toast.LENGTH_SHORT).show()
            }
        )
        with(binding) {
            rvPlayersList.layoutManager = LinearLayoutManager(context)
            rvPlayersList.adapter = adapter

        }

        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}