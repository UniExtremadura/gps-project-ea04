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
    private var _SeasonDataMap: Map<Int, NBASeasonData> = emptyMap()
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
                    _datas = fetchShows().filterNotNull().map(Data::toNBAData)
                    //val playerId = _datas.firstOrNull()?.id ?: -1
                    //Log.d("Player nombre", "Player nombre: $playerId")

                    _seasondatas = fetchSeason(_datas).filterNotNull().map(SeasonData::toSeasonAverages)

                    //val seasonpts = _seasondatas.firstOrNull()?.pts ?: -1
                    //Log.d("Season pts", "Season pts: $seasonpts")
                    //_SeasonDataMap = _seasondatas.associateBy { it.playerId ?: -1 }
                    adapter.updateData(_datas,_seasondatas)

                } catch (error: APIError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                } finally {
                    binding.spinner.visibility = View.GONE
                }
            }
        }
    }
    private suspend fun fetchSeason(Datos: List<NBAData>): List<SeasonData> {
        val apiSeasonData = mutableListOf<SeasonData>()

        for (i in Datos) {
            try {
                val playerId = i.id
                //Log.d("jug en bucle", "jug en bucle: $playerId")
                val seasonAverage = getNetworkService().getSeasonAverage(playerId).data
                //val puntos = seasonAverage.firstOrNull()?.pts ?: -1
                //Log.d("Season pts en bucle", "Season pts en bucle: $puntos")

                apiSeasonData.addAll(seasonAverage)
            } catch (e: Exception) {
                // Manejar la excepción genérica, imprime el mensaje de error
                Log.e("Error", "Error fetching season data: ${e.message}", e)
                throw APIError("Unable to fetch data from API", e)
            }
        }
        return apiSeasonData
    }

    private suspend fun fetchShows(): List<Data> {
        val apiData = mutableListOf<Data>()
        val playerIds = listOf(8, 4, 9, 12, 15, 18, 24, 28, 33, 37, 48, 53, 57, 79, 112,
            114, 115, 117, 125, 132, 140, 145, 175, 236, 237, 250, 246, 322, 434)
        try {
            for (playerId in playerIds) {
                val playerData = getNetworkService().getPlayerById(playerId)
                apiData.add(playerData)
            }
        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause)
        }

        return apiData
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