package es.unex.nbafantasy.juego.resultadoPartido

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.Data.JugadorEquipoRepository
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.Data.UsuarioJugadorRepository
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.api.getNetworkService
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityVictoriaBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

class VictoriaActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario
    private lateinit var resultadoPartido: ResultadoPartido
    private lateinit var db: BD
    private lateinit var listaJugador: List<UsuarioJugador>
    private lateinit var binding:ActivityVictoriaBinding

    private lateinit var repositoryUsuarioJugador: UsuarioJugadorRepository
    private lateinit var repositoryJugador: JugadorRepository

    companion object {
        const val USUARIO = "USUARIO"
        const val RESULTADOPARTIDO="RESULTADOPARTIDO"

        fun start(
            context: Context,
            usuario: Usuario,
            resultadoPartido: ResultadoPartido
        ) {
            val intent = Intent(context, VictoriaActivity::class.java).apply {
                putExtra(USUARIO, usuario)
                putExtra(RESULTADOPARTIDO, resultadoPartido)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victoria)

        binding = ActivityVictoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= BD.getInstance(applicationContext)!!

        repositoryJugador = JugadorRepository.getInstance(db.jugadorDao(), getNetworkService())
        repositoryUsuarioJugador = UsuarioJugadorRepository.getInstance(db.usuarioJugadorDao(),repositoryJugador)


        lifecycleScope.launch {
            usuario = (intent?.getSerializableExtra(USUARIO) as? Usuario)!!
            resultadoPartido=(intent?.getSerializableExtra(RESULTADOPARTIDO) as? ResultadoPartido)!!
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
                val x = "Mis puntos: " + resultadoPartido.misPuntos.toString()
                misPuntos.setText(x)
                val y = "Puntos rival: " + resultadoPartido.puntosRivales.toString()
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
            val x = "Mis puntos: " + resultadoPartido.misPuntos.toString()
            misPuntos.setText(x)
            val y = "Puntos rival: " + resultadoPartido.puntosRivales.toString()
            puntosRival.setText(y)
            val jugador = obtenerJugadorById(jugadorNuevo)
            val nombreApellido = getNombre(jugador.jugadorId ?: 0)
            jugadorGanado.setText(nombreApellido)

        }
    }

    private suspend fun getAllMisJugadores(): List<UsuarioJugador>{
        return repositoryUsuarioJugador.getAllMisJugadores(usuario.usuarioId?:0)
    }

    private suspend fun obtenerJugadorById(jugadorId: Long): Jugador{
        return repositoryJugador.getJugadorById(jugadorId)
    }

    private suspend fun getNombre(jugadorId:Long): String {
        val jugador = repositoryJugador.getJugadorById(jugadorId)
        return  jugador.nombre + " " + jugador.apellido
    }

    private suspend fun getUnUsuarioJugador(jugadorId: Long): UsuarioJugador{
        return repositoryUsuarioJugador.getUnUsuarioJugador(usuario.usuarioId?:0,jugadorId)
    }

    private suspend fun insertarUsuarioJugador(jugadorId:Long){
        val usuarioJugador= UsuarioJugador(usuario.usuarioId?:0, jugadorId)
        repositoryUsuarioJugador.insertarUsuarioJugador(usuarioJugador)
    }

    private fun setUpListener(){
        val btnContinuar = findViewById<Button>(R.id.bt_salir_victoria)
        btnContinuar.setOnClickListener{
            MainActivity.start(this,usuario)
        }
    }
}