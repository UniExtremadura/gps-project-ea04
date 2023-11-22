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
import es.unex.nbafantasy.juego.PantallaJuegoActivity
import kotlinx.coroutines.launch

class PantJuegoFragment : Fragment() {

    private lateinit var _binding:FragmentPantJuegoBinding
    private val binding get()=_binding!!
    private lateinit var db: BD
    private lateinit var listaEquipo: List<JugadorEquipo>
    private lateinit var usuario:Usuario
    private lateinit var listener: ListaJugadoresFragment.OnShowClickListener

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is ListaJugadoresFragment.OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentPantJuegoBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db= BD.getInstance(requireContext())!!

        setUpUi()

        binding.btJugar.setOnClickListener {
            initNav()
        }
    }

    private fun setUpUi() {
        usuario = (requireActivity() as? MainActivity)?.getUsuario()!!

        if(usuario!=null) {
            lifecycleScope.launch {
                mostrarJugadores(usuario)
            }
        }else{
            Log.e("Error", "USUARIO es nulo")
        }
    }
    private suspend fun mostrarJugadores(usuario: Usuario){
        //TODO COMPROBAR QUE HAYA 3 JUGADORES PARA PODER INICIAR PARTIDA Y VER HASTA DONDE LLEGA EL SIZE PARA MOSTRAR
        with(binding) {
            val usuarioId = usuario?.usuarioId

            if (usuarioId != null) {
                listaEquipo = db.jugadorEquipoDao().getJugadorByUsuario(usuarioId)

                if (listaEquipo.size > 0) {
                    val usuarioJugador1 = listaEquipo.get(0)
                    val nombreApellido1 =
                        db.jugadorDao().getJugadorId(usuarioJugador1.jugadorId).nombre + " " +
                                db.jugadorDao().getJugadorId(usuarioJugador1.jugadorId).apellido
                    jugadorEquipo1.setText(nombreApellido1)

                    if (listaEquipo.size > 1) {
                        val usuarioJugador2 = listaEquipo.get(1)
                        val nombreApellido2 =
                            db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).nombre + " " +
                                    db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).apellido
                        jugadorEquipo2.setText(nombreApellido2)

                        if (listaEquipo.size > 2) {
                            val usuarioJugador3 = listaEquipo.get(2)
                            val nombreApellido3 =
                                db.jugadorDao()
                                    .getJugadorId(usuarioJugador3.jugadorId).nombre + " " +
                                        db.jugadorDao()
                                            .getJugadorId(usuarioJugador3.jugadorId).apellido
                            jugadorEquipo3.setText(nombreApellido3)
                        }
                    }
                } else {
                    Log.e("Error", "usuarioId es nulo")
                }
            }
        }
    }


    private fun initNav() {
        PantallaJuegoActivity.start(context, usuario)
    }
}