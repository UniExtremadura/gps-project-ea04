package es.unex.nbafantasy.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import es.unex.nbafantasy.databinding.FragmentListaJugadoresDetailsBinding


class ListaJugadoresDetailsFragment: Fragment() {
    private var _binding: FragmentListaJugadoresDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ListaJugadoresDetailsFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaJugadoresDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nba = args.nba
        val stats = args.stats
        binding.playersName.text = nba.firstName + " " +nba.lastName
        binding.playersTeam.text = "Equipo: " + nba.team?.fullName
        binding.playersConference.text = "Conferencia: " + nba.team?.conference
        binding.playersPosition.text = "Posicion: " + nba.position
        binding.playersHeight.text = "Altura en Pulgadas: " + nba.heightInches
        binding.playersPoints.text = "Puntos Por partido: " + stats.pts
        binding.playersGames.text = "Partidos jugados: " + stats.gamesPlayed
        binding.playersBlock.text = "Tapones por partido: " + stats.blk
        binding.playersRebound.text = "Rebotes por partido: " + stats.reb
        binding.playersSteals.text = "Robos por partido: " + stats.stl
        binding.playersAsist.text = "Asistencias por partido: " + stats.ast
        binding.playersMin.text = "Minutos por partido: " + stats.min
        binding.playersTurnover.text = "Errores por partido: " + stats.turnover
        binding.playersStats.text = "Media general: "
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListaJugadoresDetailsFragment()
    }
}