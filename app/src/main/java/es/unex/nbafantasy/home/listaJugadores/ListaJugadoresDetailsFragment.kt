package es.unex.nbafantasy.home.listaJugadores

import android.os.Bundle
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
        binding.playersName.text = nba.nombre + " " +nba.apellido
        binding.playersTeam.text = "Equipo: " + nba.equipo
        binding.playersConference.text = "Conferencia: " + nba.conferencia
        binding.playersPosition.text = "Posicion: " + nba.posicion
        binding.playersHeight.text = "Altura en Pulgadas: " + nba.alturaPulgadas
        binding.playersPoints.text = "Puntos Por partido: " + nba.puntosPartido
        binding.playersBlock.text = "Tapones por partido: " + nba.taponesPartido
        binding.playersRebound.text = "rebotes por partido: " + nba.rebotesPartido
        binding.playersAsist.text = "asistencias por partido: " + nba.asistenciasPartido
        binding.playersStats.text = "Media general: " + nba.mediaGeneralPartido
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListaJugadoresDetailsFragment()
    }
}