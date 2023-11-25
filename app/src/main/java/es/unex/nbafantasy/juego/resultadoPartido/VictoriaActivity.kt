package es.unex.nbafantasy.juego.resultadoPartido

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.api.APIError
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
    private lateinit var listaUsuarioJugador: List<UsuarioJugador>
    private lateinit var listaJugador: List<Jugador>
    private lateinit var binding:ActivityVictoriaBinding

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
        listaJugador = db.jugadorDao().getAll()
        val numJugadores = listaJugador.size

        var jugadorNuevo = -1
        if (db.usuarioJugadorDao().getAll().size == 29) {
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
                    if (db.usuarioJugadorDao().getUnUsuarioJugador(usuario.usuarioId ?: 0, jugadorNuevo.toLong()) == null) {
                        val usuarioJugador = UsuarioJugador(usuario.usuarioId ?: 0, jugadorNuevo.toLong())
                        Log.d("USUARIO ID", "USUARIO ID: ${usuarioJugador.usuarioId}")
                        Log.d("equipo ID", "equipo ID: ${usuarioJugador.jugadorId}")
                        db.usuarioJugadorDao().insertar(usuarioJugador)
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
            val jugador = db.jugadorDao().getJugadorId(jugadorNuevo)
            val nombreApellido =
                db.jugadorDao().getJugadorId(jugador.jugadorId ?: 0).nombre + " " +
                        db.jugadorDao().getJugadorId(jugador.jugadorId ?: 0).apellido
            jugadorGanado.setText(nombreApellido)

        }
    }
    private fun setUpListener(){
        val btnContinuar = findViewById<Button>(R.id.bt_salir_victoria)
        btnContinuar.setOnClickListener{
            MainActivity.start(this,usuario)
        }
    }
}


