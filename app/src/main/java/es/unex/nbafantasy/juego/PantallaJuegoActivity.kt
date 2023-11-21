package es.unex.nbafantasy.juego

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityPantallaJuegoBinding
import es.unex.nbafantasy.juego.resultadoPartido.VictoriaActivity
import kotlinx.coroutines.launch


class PantallaJuegoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPantallaJuegoBinding
    private lateinit var db: BD
    private lateinit var listaUsuarioJugador: List<JugadorEquipo>
    private lateinit var text: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= BD.getInstance(applicationContext)!!
        val usuario = intent?.getSerializableExtra(DarCartaActivity.USUARIO) as? Usuario

        if(usuario!=null) {
            lifecycleScope.launch {
                //mostrarJugadores(usuario)
                setUpListener()
            }
        }else{
            Log.e("Error", "USUARIO es nulo")
        }

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
                text=findViewById(R.id.jugadorEquipo1)
                text.setText(nombreApellido1)

                val usuarioJugador2 = listaUsuarioJugador.get(1)
                val nombreApellido2 =
                    db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador2.jugadorId).apellido
                text=findViewById(R.id.jugadorEquipo2)
                text.setText(nombreApellido2)

                val usuarioJugador3 = listaUsuarioJugador.get(2)
                val nombreApellido3 =
                    db.jugadorDao().getJugadorId(usuarioJugador3.jugadorId).nombre + " " +
                            db.jugadorDao().getJugadorId(usuarioJugador3.jugadorId).apellido
                text=findViewById(R.id.jugadorEquipo3)
                text.setText(nombreApellido3)



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

    private fun setUpListener(){
        val btnContinuar = findViewById<Button>(R.id.bt_comenzar_partida)
        btnContinuar.setOnClickListener{
            val intent = Intent (this, VictoriaActivity::class.java)
            //val intent = Intent (this, DerrotaActivity::class.java)
            startActivity(intent)
        }
    }
}