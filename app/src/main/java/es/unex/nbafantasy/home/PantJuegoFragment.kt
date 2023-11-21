package es.unex.nbafantasy.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.FragmentPantJuegoBinding
import es.unex.nbafantasy.juego.DarCartaActivity
import kotlinx.coroutines.launch

class PantJuegoFragment : Fragment() {

    private lateinit var _binding:FragmentPantJuegoBinding
    private val binding get()=_binding!!
    private lateinit var db: BD
    private lateinit var listaUsuarioJugador: List<JugadorEquipo>
    private lateinit var text: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentPantJuegoBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()

    }

    private fun setUpUi() {
        db= BD.getInstance(requireContext())!!
        val usuario = (requireActivity() as? MainActivity)?.getUsuario()

        if(usuario!=null) {
            lifecycleScope.launch {
                mostrarJugadores(usuario)
            }
        }else{
            Log.e("Error", "USUARIO es nulo")
        }

        initNav()
    }
    private suspend fun mostrarJugadores(usuario: Usuario){
        with(binding) {
            val usuarioId = usuario?.usuarioId

            if (usuarioId != null) {
                listaUsuarioJugador = db.jugadorEquipoDao().getJugadorByUsuario(usuarioId)

                val usuarioJugador1 = listaUsuarioJugador.get(0)
                val nombreApellido1 =
                    db.jugadorDao().getJugadorId(usuarioJugador1.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador1.jugadorId).apellido
                jugadorEquipo1.setText(nombreApellido1)

                val usuarioJugador2 = listaUsuarioJugador.get(1)
                val nombreApellido2 =
                    db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).apellido
                jugadorEquipo2.setText(nombreApellido2)

                val usuarioJugador3 = listaUsuarioJugador.get(2)
                val nombreApellido3 =
                    db.jugadorDao().getJugadorId(usuarioJugador3.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador3.jugadorId).apellido
                jugadorEquipo3.setText(nombreApellido3)



                /*val nombreApellido1 =
                db.jugadorDao().getJugadorId(posicionJugador1).nombre + " " +
                        db.jugadorDao().getJugadorId(posicionJugador1).apellido
            playersName1.setText(nombreApellido1)

            val nombreApellido2 =
                db.jugadorDao().getJugadorId(posicionJugador2).nombre + " " +
                        db.jugadorDao().getJugadorId(posicionJugador2).apellido
            playersName2.setText(nombreApellido2)

            val nombreApellido3 =
                db.jugadorDao().getJugadorId(posicionJugador3).nombre + " " +
                        db.jugadorDao().getJugadorId(posicionJugador3).apellido
            playersName3.setText(nombreApellido3)*/
            }else{
                Log.e("Error", "usuarioId es nulo")
            }
        }
    }

    private fun initNav() {
        binding.btJugar.setOnClickListener {
            findNavController().navigate(
                PantJuegoFragmentDirections.actionPantJuegoFragmentToPantallaJuegoActivity()
            )
        }
    }
}