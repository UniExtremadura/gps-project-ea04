package es.unex.nbafantasy.juego

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityPantallaJuegoBinding
import es.unex.nbafantasy.juego.resultadoPartido.DerrotaActivity
import es.unex.nbafantasy.juego.resultadoPartido.VictoriaActivity
import kotlinx.coroutines.launch
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
                val puntosMiEquipo = mostrarMiEquipo(usuario)
                val puntosRival= mostrarRival()
                val resultado = puntosMiEquipo - puntosRival
                Log.e("Error", " puntosMiEquipo $puntosMiEquipo - puntosRival $puntosRival")
                val btnContinuar = findViewById<Button>(R.id.bt_comenzar_partida)

                btnContinuar.setOnClickListener {
                    Log.e("Error", " RESULTADO: $resultado")
                    setUpListener(resultado,puntosMiEquipo,puntosRival)
                }

            }
        }else{
            Log.e("Error", "USUARIO es nulo")
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

        var posicionJugador1 = random.nextInt(numJugadores)
        if(posicionJugador1==0) posicionJugador1=posicionJugador1+1

        var posicionJugador2 = random.nextInt(numJugadores)
        if(posicionJugador2==0) posicionJugador2=posicionJugador2+1

        var posicionJugador3 = random.nextInt(numJugadores)
        if(posicionJugador3==0) posicionJugador3=posicionJugador3+1

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
        if(resultado>0){
                resultadoPartido =
                    ResultadoPartido(null, usuario.usuarioId?:0, puntosMiEquipo, puntosRival, "Victoria")
                VictoriaActivity.start(this, usuario, resultadoPartido)
        }else{
            resultadoPartido =
                ResultadoPartido(null, usuario.usuarioId?:0, puntosMiEquipo, puntosRival, "Derrota")
            DerrotaActivity.start(this,usuario,resultadoPartido)
        }
    }
}