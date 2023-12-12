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
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.Data.api.Data
import es.unex.nbafantasy.Data.model.NBAData
import es.unex.nbafantasy.Data.model.NBASeasonData
import es.unex.nbafantasy.Data.toSeasonAverages
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.api.getNetworkService
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityDarCartaBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

class DarCartaActivity : AppCompatActivity() {
    private var _datas: List<NBAData> = emptyList()
    private var _seasondatas: List<NBASeasonData> = emptyList()
    private lateinit var repository: JugadorRepository
    private lateinit var db: BD
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
        db= BD.getInstance(applicationContext)!!
        repository = JugadorRepository.getInstance(db.jugadorDao(),getNetworkService())

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
        val numJugadoresEnBD = db?.jugadorDao()?.countJugadores() ?: 0
        if (_datas.isEmpty() && _seasondatas.isEmpty() && numJugadoresEnBD == 0) {
            binding.carga.visibility = View.VISIBLE
            binding.playersName1.visibility = View.GONE
            binding.playersName2.visibility = View.GONE
            binding.playersName3.visibility = View.GONE
            binding.btAceptar.visibility = View.GONE
            try {
                _datas = repository.fetchRecentPlayers()
                _seasondatas = repository.fetchSeason(_datas).map(SeasonData::toSeasonAverages)
                repository.ObtenerJugadores(_datas,_seasondatas)
                /*var i = 0;
                for (playerId in _datas) {
                    val media = (_seasondatas[i].pts * pesoPuntos +
                            _seasondatas[i].blk * pesoTapones +
                            _seasondatas[i].reb * pesoRebotes +
                            _seasondatas[i].stl * pesoRobos +
                            _seasondatas[i].ast * pesoAsistencias -
                            _seasondatas[i].turnover * pesoErrores)*10
                    val jugador = Jugador(
                        null,
                        playerId.firstName,
                        playerId.lastName,
                        playerId.team?.fullName,
                        playerId.team?.conference,
                        playerId.position,
                        playerId.heightInches?.toDouble(),
                        _seasondatas[i].pts,
                        _seasondatas[i].blk,
                        _seasondatas[i].reb,
                        _seasondatas[i].stl,
                        _seasondatas[i].ast,
                        _seasondatas[i].min,
                        _seasondatas[i].turnover,
                        media.round(2)
                    )
                    db?.jugadorDao()?.insertar(jugador)
                    i=i+1;
                }*/
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

    suspend fun fetchShows(): List<Data> {
        val apiData = mutableListOf<Data>()
        val playerIds = listOf(8, 4, 9, 12, 15, 18, 24, 28, 33, 37, 48, 53, 57, 79, 112,
            114, 115, 117, 125, 132, 140, 145, 175, 236, 237, 250, 246, 322, 434)
        try {
            for (playerId in playerIds) {
                val playerData = getNetworkService().getPlayerById(playerId)
                apiData.add(playerData)
            }
        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause)
        }

        return apiData
    }

    private suspend fun fetchSeason(Datos: List<NBAData>): List<SeasonData> {
        val apiSeasonData = mutableListOf<SeasonData>()

        for (i in Datos) {
            try {
                val playerId = i.id
                val seasonAverage = getNetworkService().getSeasonAverage(playerId).data

                apiSeasonData.addAll(seasonAverage)
            } catch (e: Exception) {
                throw APIError("Unable to fetch data from API", e)
            }
        }
        return apiSeasonData
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

        val numJugadores = (db.jugadorDao().getAll()).size
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
        db.usuarioJugadorDao().insertar(usuarioJugador)
    }

    private suspend fun insertarJugadorEquipo(posicionJugador: Int) {
        val jugadorEquipo = JugadorEquipo(usuario.usuarioId?:0, posicionJugador.toLong())
        db.jugadorEquipoDao().insertar(jugadorEquipo)
    }

    private suspend fun mostrarNombresJugadores(posicionJugador: Int, textView: TextView) {
        val jugador = db.jugadorDao().getJugadorId(posicionJugador.toLong())
        val nombreApellido = "${jugador.nombre} ${jugador.apellido}"
        textView.text = nombreApellido
    }
    private fun navegarPantallaPrincipal(usuario:Usuario, mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
        MainActivity.start(this,usuario)
    }

}