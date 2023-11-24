package es.unex.nbafantasy.juego

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityPantallaJuegoBinding
import es.unex.nbafantasy.juego.resultadoPartido.DerrotaActivity
import es.unex.nbafantasy.juego.resultadoPartido.VictoriaActivity
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random


class PantallaJuegoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPantallaJuegoBinding
    private lateinit var db: BD
    private lateinit var listaEquipo: List<JugadorEquipo>
    private lateinit var listaJugador: List<Jugador>
    private lateinit var usuario: Usuario
    private lateinit var resultadoPartido: ResultadoPartido


    companion object{
        const val USUARIO="USUARIO"

        fun start(
            context: Context?,
            usuario: Usuario,
        ){
            val intent= Intent(context, PantallaJuegoActivity::class.java).apply {
                putExtra(USUARIO, usuario)
            }
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= BD.getInstance(applicationContext)!!
        usuario = (intent?.getSerializableExtra(USUARIO) as? Usuario)!!

        if(usuario!=null) {
            lifecycleScope.launch {

                val puntosMiEquipo = mostrarMiEquipo(usuario).round(2)
                val puntosRival= mostrarRival().round(2)
                val resultado = (puntosMiEquipo - puntosRival).round(2)

                val btnContinuar = findViewById<Button>(R.id.bt_comenzar_partida)
                btnContinuar.setOnClickListener {
                    setUpListener(resultado,puntosMiEquipo,puntosRival)
                }
            }
        }
    }

    private suspend fun mostrarMiEquipo(usuario: Usuario):Double{
        with(binding) {
            val usuarioId = usuario?.usuarioId

            if (usuarioId != null) {
                listaEquipo = db.jugadorEquipoDao().getJugadorByUsuario(usuarioId)

                val usuarioJugador1 = listaEquipo.get(0)
                val nombreApellido1 =
                    db.jugadorDao().getJugadorId(usuarioJugador1.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador1.jugadorId).apellido
                miJugador1.setText(nombreApellido1)

                val usuarioJugador2 = listaEquipo.get(1)
                val nombreApellido2 =
                    db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).apellido
                miJugador2.setText(nombreApellido2)

                val usuarioJugador3 = listaEquipo.get(2)
                val nombreApellido3 =
                    db.jugadorDao().getJugadorId(usuarioJugador3.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador3.jugadorId).apellido
                miJugador3.setText(nombreApellido3)

                val sumaTotal= db.jugadorDao().getJugadorId(usuarioJugador1.jugadorId).mediaGeneralPartido +
                        db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).mediaGeneralPartido +
                        db.jugadorDao().getJugadorId(usuarioJugador3.jugadorId).mediaGeneralPartido

                return sumaTotal
            }else{
                Log.e("Error", "usuarioId es nulo")
            }
        }
        return 0.0
    }

    private suspend fun mostrarRival():Double{
        val random= Random(System.currentTimeMillis())
        listaJugador = db.jugadorDao().getAll()
        val numJugadores = listaJugador.size
        //Log.d("AAAAAAAAAA", "TAM jugadores ${listaJugador.size}")


        var posicionJugador1 = random.nextInt(numJugadores)+1

        var posicionJugador2 = 0
        do{
            posicionJugador2 = random.nextInt(numJugadores)+1
        }while(posicionJugador1 ==posicionJugador2)

        var posicionJugador3 = 0
        do{
            posicionJugador3 = random.nextInt(numJugadores)+1
        }while(posicionJugador1 ==posicionJugador3 && posicionJugador2 ==posicionJugador3)

        with(binding) {
            val usuarioJugador1=db.jugadorDao().getJugadorId(posicionJugador1.toLong()).jugadorId
            val nombreApellido1 =
                db.jugadorDao().getJugadorId(posicionJugador1.toLong()).nombre + " " +
                        db.jugadorDao().getJugadorId(posicionJugador1.toLong()).apellido
            equipoContrarioJugador1.setText(nombreApellido1)

            val usuarioJugador2=db.jugadorDao().getJugadorId(posicionJugador2.toLong()).jugadorId
            val nombreApellido2 =
                db.jugadorDao().getJugadorId(posicionJugador2.toLong()).nombre + " " +
                        db.jugadorDao().getJugadorId(posicionJugador2.toLong()).apellido
            equipoContrarioJugador2.setText(nombreApellido2)

            val usuarioJugador3=db.jugadorDao().getJugadorId(posicionJugador3.toLong()).jugadorId
            val nombreApellido3 =
                db.jugadorDao().getJugadorId(posicionJugador3.toLong()).nombre + " " +
                        db.jugadorDao().getJugadorId(posicionJugador3.toLong()).apellido
            equipoContrarioJugador3.setText(nombreApellido3)

            if(usuarioJugador1!=null && usuarioJugador2!=null && usuarioJugador3!=null) {
                val sumaTotal = db.jugadorDao().getJugadorId(usuarioJugador1).mediaGeneralPartido +
                        db.jugadorDao().getJugadorId(usuarioJugador2).mediaGeneralPartido +
                        db.jugadorDao().getJugadorId(usuarioJugador3).mediaGeneralPartido
                return sumaTotal
            }
        }
        return 0.0
    }

    private fun setUpListener(resultado:Double, puntosMiEquipo:Double,puntosRival:Double){


        if (resultado > 0) {
            resultadoPartido = ResultadoPartido(null,usuario.usuarioId ?: 0,
                puntosMiEquipo, puntosRival,"Victoria")

            lifecycleScope.launch {
                db.resultadoPartidoDao().insertar(resultadoPartido)
            }

            VictoriaActivity.start(this, usuario, resultadoPartido)
        } else {
            resultadoPartido = ResultadoPartido(null,usuario.usuarioId ?: 0,
                puntosMiEquipo, puntosRival,"Derrota")

            lifecycleScope.launch {
                db.resultadoPartidoDao().insertar(resultadoPartido)
            }

            DerrotaActivity.start(this, usuario, resultadoPartido)
        }
    }


    fun Double.round(decimales: Int): Double {
        val factor = 10.0.pow(decimales.toDouble())
        return (this * factor).roundToInt() / factor
    }

}