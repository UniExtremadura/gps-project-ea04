package es.unex.nbafantasy.juego

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.example.SeasonData
import es.unex.nbafantasy.data.JugadorEquipoRepository
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.data.UsuarioJugadorRepository
import es.unex.nbafantasy.data.model.NBAData
import es.unex.nbafantasy.data.model.NBASeasonData
import es.unex.nbafantasy.data.toSeasonAverages
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.databinding.ActivityDarCartaBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

class DarCartaActivity : AppCompatActivity() {
    private var _datas: List<NBAData> = emptyList()
    private var _seasondatas: List<NBASeasonData> = emptyList()
    private lateinit var repositoryJugador: JugadorRepository
    private lateinit var repositoryUsuarioJugador: UsuarioJugadorRepository
    private lateinit var repositoryJugadorEquipo: JugadorEquipoRepository
    private lateinit var binding:ActivityDarCartaBinding
    private lateinit var listaJugador: List<Jugador>
    private lateinit var usuario: Usuario


    companion object{
        const val USUARIO="USUARIO"

        fun start(
            context: Context,
            usuario: Usuario,
        ){
            val intent= Intent(context, DarCartaActivity::class.java).apply {
                putExtra(USUARIO, usuario)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDarCartaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializacion BD
        val appContainer = (this.application as NBAFantasyApplication).appContainer

        repositoryJugador = appContainer.repositoryJugador
        repositoryUsuarioJugador = appContainer.repositoryUsuarioJugador
        repositoryJugadorEquipo = appContainer.repositoryJugadorEquipo

        usuario = intent?.getSerializableExtra(USUARIO) as Usuario

        if(usuario!=null) {
            lifecycleScope.launch {
                setUpUI()
                kotlinx.coroutines.delay(50)

                Log.e("API CARGADA", "La API se ha cargado")

                obtenerJugadores()

                // Verificar si el usuario no es nulo
                with(binding) {
                    btAceptar.setOnClickListener {
                        if (usuario != null) {
                            // Llamar a la función de navegación con el usuario
                            navegarPantallaPrincipal(usuario, "Jugadores Recibidos")
                        }
                    }
                }
            }
        }
    }


    private suspend fun setUpUI() {
        val numJugadoresEnBD = getAll().size
        if (_datas.isEmpty() && _seasondatas.isEmpty() && numJugadoresEnBD == 0) {
            binding.carga.visibility = View.VISIBLE
            binding.playersName1.visibility = View.GONE
            binding.playersName2.visibility = View.GONE
            binding.playersName3.visibility = View.GONE
            binding.btAceptar.visibility = View.GONE
            try {
                _datas = repositoryJugador.fetchRecentPlayers()
                _seasondatas = repositoryJugador.fetchSeason(_datas).map(SeasonData::toSeasonAverages)
                repositoryJugador.ObtenerJugadores(_datas,_seasondatas)
            } catch (error: APIError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            } finally {
                binding.carga.visibility = View.GONE
                binding.playersName1.visibility = View.VISIBLE
                binding.playersName2.visibility = View.VISIBLE
                binding.playersName3.visibility = View.VISIBLE
                binding.btAceptar.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun getAll(): List<Jugador>{
        return repositoryJugador.getAll()
    }

    private suspend fun obtenerJugadores() {
        try {
            val usuarioId = usuario?.usuarioId

            if (usuarioId != null) {

                val posicionJugador1 = obtenerPosicionAleatoria()
                val posicionJugador2 = obtenerPosicionAleatoria(posicionJugador1)
                val posicionJugador3 = obtenerPosicionAleatoria(posicionJugador1, posicionJugador2)

                insertarUsuarioJugador(posicionJugador1)
                insertarUsuarioJugador(posicionJugador2)
                insertarUsuarioJugador(posicionJugador3)

                insertarJugadorEquipo(posicionJugador1)
                insertarJugadorEquipo(posicionJugador2)
                insertarJugadorEquipo(posicionJugador3)

                mostrarNombresJugadores(posicionJugador1, binding.playersName1)
                mostrarNombresJugadores(posicionJugador2, binding.playersName2)
                mostrarNombresJugadores(posicionJugador3, binding.playersName3)
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al insertar en UsuarioJugador: ${e.message}", e)
        }
    }

    private suspend fun obtenerPosicionAleatoria(vararg exclusiones: Int): Int {
        val numJugadores = getAll().size
        val random = Random(System.currentTimeMillis())
        val listaExclusiones = exclusiones.toList()

        var posicion: Int
        do {
            posicion = random.nextInt(numJugadores) + 1
        } while (posicion in listaExclusiones)

        return posicion
    }

    private suspend fun insertarUsuarioJugador(posicionJugador: Int) {
        val usuarioJugador = UsuarioJugador(usuario.usuarioId?:0, posicionJugador.toLong())
        repositoryUsuarioJugador.insertarUsuarioJugador(usuarioJugador)
    }

    private suspend fun insertarJugadorEquipo(posicionJugador: Int) {
        repositoryJugadorEquipo.insertar(usuario.usuarioId?:0, posicionJugador.toLong())
    }

    private suspend fun mostrarNombresJugadores(posicionJugador: Int, textView: TextView) {
        val jugador = obtenerJugadorById(posicionJugador.toLong())
        val nombreApellido = "${jugador.nombre} ${jugador.apellido}"
        textView.text = nombreApellido
    }

    private suspend fun obtenerJugadorById(jugadorId: Long): Jugador{
        return repositoryJugador.getJugadorById(jugadorId)
    }

    private fun navegarPantallaPrincipal(usuario:Usuario, mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
        MainActivity.start(this,usuario)
    }

}