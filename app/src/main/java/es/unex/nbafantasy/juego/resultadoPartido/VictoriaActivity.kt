package es.unex.nbafantasy.juego.resultadoPartido

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.data.UsuarioJugadorRepository
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.databinding.ActivityVictoriaBinding
import es.unex.nbafantasy.home.resultado.ResultadoViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

class VictoriaActivity : AppCompatActivity() {
    //private lateinit var resultadoPartido: ResultadoPartido
    private lateinit var listaJugador: List<UsuarioJugador>
    private lateinit var binding:ActivityVictoriaBinding

    private val viewModel: VictoriaViewModel by viewModels { VictoriaViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victoria)

        binding = ActivityVictoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appContainer = (this.application as NBAFantasyApplication).appContainer


        lifecycleScope.launch {
            //viewModel.usuario = (intent?.getSerializableExtra(USUARIO) as? Usuario)!!
            viewModel.getUsuario()
            viewModel.getResultado()
            //resultadoPartido=(intent?.getSerializableExtra(RESULTADOPARTIDO) as? ResultadoPartido)!!
            darCarta()
            setUpListener()
        }
    }

    private suspend fun darCarta(){
        var valido = false
        val random = Random(System.currentTimeMillis())
        listaJugador = getAllMisJugadores()
        val numJugadores = listaJugador.size

        var jugadorNuevo: Int
        if (listaJugador.size == 29) {
            with(binding) {
                val x = "Mis puntos: " + viewModel.Resultado!!.misPuntos.toString()
                misPuntos.setText(x)
                val y = "Puntos rival: " + viewModel.Resultado!!.puntosRivales.toString()
                puntosRival.setText(y)
                jugadorGanado.setText("Has conseguido a todos")
            }
        } else {
            do {
                jugadorNuevo = random.nextInt(numJugadores) + 1
                if(jugadorNuevo!=null){
                    if (getUnUsuarioJugador(jugadorNuevo.toLong()) == null) {
                        insertarUsuarioJugador(jugadorNuevo.toLong())
                        valido = true
                    }
                }
            } while (valido == false)
            mostrarText(jugadorNuevo.toLong())
        }
    }

    private suspend fun mostrarText(jugadorNuevo:Long){
        with(binding) {
            val x = "Mis puntos: " + viewModel.Resultado!!.misPuntos.toString()
            misPuntos.setText(x)
            val y = "Puntos rival: " + viewModel.Resultado!!.puntosRivales.toString()
            puntosRival.setText(y)
            val jugador = obtenerJugadorById(jugadorNuevo)
            val nombreApellido = getNombre(jugador.jugadorId ?: 0)
            jugadorGanado.setText(nombreApellido)

        }
    }

    suspend fun getAllMisJugadores(): List<UsuarioJugador>{
        return viewModel.getAllMisJugadores()
    }

    suspend fun obtenerJugadorById(jugadorId: Long): Jugador{
        return viewModel.getJugadorById(jugadorId)
    }

    suspend fun getNombre(jugadorId:Long): String {
        val jugador = viewModel.getJugadorById(jugadorId)
        return  jugador.nombre + " " + jugador.apellido
    }

    suspend fun getUnUsuarioJugador(jugadorId: Long): UsuarioJugador{
        return viewModel.getUnUsuarioJugador(jugadorId)
    }

    private fun insertarUsuarioJugador(jugadorId:Long){
        val usuarioJugador= UsuarioJugador(viewModel.usuario!!.usuarioId!!, jugadorId)
        viewModel.insertarUsuarioJugador(usuarioJugador)
    }

    private fun setUpListener(){
        val btnContinuar = findViewById<Button>(R.id.bt_salir_victoria)
        btnContinuar.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent )
        }
    }
}