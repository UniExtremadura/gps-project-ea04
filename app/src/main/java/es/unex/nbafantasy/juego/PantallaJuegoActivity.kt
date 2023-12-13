package es.unex.nbafantasy.juego

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.Data.JugadorEquipoRepository
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.Data.ResultadoPartidoRepository
import es.unex.nbafantasy.R
import es.unex.nbafantasy.api.getNetworkService
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

    private lateinit var repositoryResultadoPartido: ResultadoPartidoRepository
    private lateinit var repositoryJugadorEquipo: JugadorEquipoRepository
    private lateinit var repositoryJugador: JugadorRepository

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

        repositoryJugador = JugadorRepository.getInstance(db.jugadorDao(),getNetworkService())
        repositoryJugadorEquipo = JugadorEquipoRepository.getInstance(db.jugadorEquipoDao())
        repositoryResultadoPartido = ResultadoPartidoRepository.getInstance(db.resultadoPartidoDao())

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
    private suspend fun mostrarMiEquipo(usuario: Usuario): Double {
        with(binding) {
            val usuarioId = usuario?.usuarioId

            if (usuarioId != null) {
                listaEquipo = obtenerListaJugadores()

                mostrarJugadorEnTextView(0, miJugador1)
                mostrarJugadorEnTextView(1, miJugador2)
                mostrarJugadorEnTextView(2, miJugador3)

                val sumaTotal = calcularSumaTotal()
                return sumaTotal
            }
        }
        return 0.0
    }

    private suspend fun obtenerListaJugadores(): List<JugadorEquipo>{
        return repositoryJugadorEquipo.getJugadoresUsuario(usuario.usuarioId?:0)
    }

    private suspend fun mostrarJugadorEnTextView(index: Int, textView: TextView) {
        val usuarioJugador = listaEquipo.getOrNull(index)
        if (usuarioJugador != null) {
            val jugadorId = usuarioJugador.jugadorId
            val nombreApellido = getNombre(jugadorId)

            textView.text = nombreApellido
        }
    }

    private suspend fun calcularSumaTotal(): Double {
        val jugador1 = listaEquipo.getOrNull(0)
        val jugador2 = listaEquipo.getOrNull(1)
        val jugador3 = listaEquipo.getOrNull(2)


        val mediaJugador1 = jugador1?.let { getJugadorById(it.jugadorId).mediaGeneralPartido } ?: 0.0
        val mediaJugador2 = jugador2?.let { getJugadorById(it.jugadorId).mediaGeneralPartido } ?: 0.0
        val mediaJugador3 = jugador3?.let { getJugadorById(it.jugadorId).mediaGeneralPartido } ?: 0.0

        return mediaJugador1 + mediaJugador2 + mediaJugador3
    }

    private suspend fun mostrarRival():Double{
        val random= Random(System.currentTimeMillis())
        listaJugador = getAll()

        val numJugadores = listaJugador.size

        var posicionJugador1 = random.nextInt(numJugadores)+1

        var posicionJugador2: Int
        do{
            posicionJugador2 = random.nextInt(numJugadores)+1
        }while(posicionJugador1 ==posicionJugador2)

        var posicionJugador3: Int
        do{
            posicionJugador3 = random.nextInt(numJugadores)+1
        }while(posicionJugador1 ==posicionJugador3 || posicionJugador2 ==posicionJugador3)

        with(binding) {
            val usuarioJugador1=getJugadorById(posicionJugador1.toLong())
            val nombreApellido1=getNombre(usuarioJugador1.jugadorId?:0.toLong())
            equipoContrarioJugador1.setText(nombreApellido1)

            val usuarioJugador2=getJugadorById(posicionJugador2.toLong())
            val nombreApellido2=getNombre(usuarioJugador2.jugadorId?:0.toLong())
            equipoContrarioJugador2.setText(nombreApellido2)

            val usuarioJugador3=getJugadorById(posicionJugador3.toLong())
            val nombreApellido3=getNombre(usuarioJugador3.jugadorId?:0.toLong())
            equipoContrarioJugador3.setText(nombreApellido3)


            if(usuarioJugador1!=null && usuarioJugador2!=null && usuarioJugador3!=null) {
                val sumaTotal = usuarioJugador1.mediaGeneralPartido + usuarioJugador2.mediaGeneralPartido +
                        usuarioJugador3.mediaGeneralPartido
                return sumaTotal
            }
        }
        return 0.0
    }

    private suspend fun getAll(): List<Jugador>{
        return repositoryJugador.getAll()
    }

    private suspend fun getJugadorById(jugadorId: Long): Jugador{
        return repositoryJugador.getJugadorById(jugadorId)
    }

    private suspend fun getNombre(jugadorId:Long): String {
        val jugador = repositoryJugador.getJugadorById(jugadorId)
        return  jugador.nombre + " " + jugador.apellido
    }

    private fun setUpListener(resultado:Double, puntosMiEquipo:Double,puntosRival:Double){
        if (resultado > 0) {
            resultadoPartido = ResultadoPartido(null,usuario.usuarioId ?: 0,
                puntosMiEquipo, puntosRival,"Victoria")

            lifecycleScope.launch {
                repositoryResultadoPartido.insertarResultado(resultadoPartido)
            }

            VictoriaActivity.start(this, usuario, resultadoPartido)
        } else {
            resultadoPartido = ResultadoPartido(null,usuario.usuarioId ?: 0,
                puntosMiEquipo, puntosRival,"Derrota")

            lifecycleScope.launch {
                repositoryResultadoPartido.insertarResultado(resultadoPartido)
            }

            DerrotaActivity.start(this, usuario, resultadoPartido)
        }
    }


    fun Double.round(decimales: Int): Double {
        val factor = 10.0.pow(decimales.toDouble())
        return (this * factor).roundToInt() / factor
    }

}