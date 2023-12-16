package es.unex.nbafantasy.home.pantJuego

import PantJuegoViewModel
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainViewModel
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.databinding.FragmentPantJuegoBinding
import es.unex.nbafantasy.juego.PantallaJuegoActivity
import kotlinx.coroutines.launch

class PantJuegoFragment : Fragment() {

    private lateinit var _binding:FragmentPantJuegoBinding
    private val binding get()=_binding!!
    private lateinit var listaEquipo: List<JugadorEquipo>

    private val viewModel: PantJuegoViewModel by viewModels { PantJuegoViewModel.Factory }
    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentPantJuegoBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.usuario.observe(viewLifecycleOwner){usuario ->
            viewModel.usuario=usuario
            setUpUi()
            pulsarBoton()
        }
    }

    private fun pulsarBoton(){
        binding.btJugar.setOnClickListener {
            lifecycleScope.launch {
                val equipoSize =obtenerListaJugadores().size
                if (equipoSize == 3) {
                    initNav()
                } else {
                    Toast.makeText(
                        getContext(),
                        "Debes tener 3 jugadores para poder jugar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setUpUi() {
        if(viewModel.usuario!=null) {
            lifecycleScope.launch {
                mostrarJugadores(viewModel.usuario!!)
            }
        }
    }
    private suspend fun mostrarJugadores(usuario: Usuario){
        with(binding) {
            val usuarioId = usuario?.usuarioId

            if (usuarioId != null) {
                listaEquipo = obtenerListaJugadores()

                if (listaEquipo.size > 0) {
                    val usuarioJugador1 = listaEquipo.get(0)
                    val nombreApellido1 = obtenerNombre(usuarioJugador1.jugadorId)
                    jugadorEquipo1.setText(nombreApellido1)

                    if (listaEquipo.size > 1) {
                        val usuarioJugador2 = listaEquipo.get(1)
                        val nombreApellido2 = obtenerNombre(usuarioJugador2.jugadorId)
                        jugadorEquipo2.setText(nombreApellido2)

                        if (listaEquipo.size > 2) {
                            val usuarioJugador3 = listaEquipo.get(2)
                            val nombreApellido3 = obtenerNombre(usuarioJugador3.jugadorId)
                            jugadorEquipo3.setText(nombreApellido3)
                        }
                    }
                }
            }
        }
    }

    private suspend fun obtenerListaJugadores(): List<JugadorEquipo>{
        return viewModel.obtenerListaJugadores()
    }

    private suspend fun obtenerNombre(jugadorId:Long): String {
        val jugador = viewModel.getJugadorById(jugadorId)
        return  jugador.nombre + " " + jugador.apellido
    }

    private fun initNav() {
        val intent = Intent (context, PantallaJuegoActivity::class.java)
        startActivity(intent)
    }
}