package es.unex.nbafantasy.juego

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.example.SeasonData
import es.unex.nbafantasy.Data.api.Data
import es.unex.nbafantasy.Data.model.NBAData
import es.unex.nbafantasy.Data.model.NBASeasonData
import es.unex.nbafantasy.Data.toNBAData
import es.unex.nbafantasy.Data.toSeasonAverages
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.api.getNetworkService
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityDarCartaBinding
import es.unex.nbafantasy.databinding.ActivityRegistroBinding
import kotlinx.coroutines.launch

class DarCartaActivity : AppCompatActivity() {
    private var _datas: List<NBAData> = emptyList()
    private var _seasondatas: List<NBASeasonData> = emptyList()
    private lateinit var db: BD
    private lateinit var binding:ActivityDarCartaBinding
    private var pesoPuntos = 0.2
    private var pesoTapones = 0.1
    private var pesoRebotes = 0.15
    private var pesoRobos = 0.15
    private var pesoAsistencias = 0.2
    private var pesoErrores = 0.1
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
        setUpUI()
        //DarCarta()
        // Obtener el usuario del intent
        val usuario = intent?.getSerializableExtra(USUARIO) as Usuario

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

    private fun DarCarta() {
        TODO("Not yet implemented")
    }

    private fun setUpUI() {
        lifecycleScope.launch {
            if (_datas.isEmpty() && _seasondatas.isEmpty()) {
                try {
                    _datas = fetchShows().filterNotNull().map(Data::toNBAData)
                    _seasondatas = fetchSeason(_datas).filterNotNull().map(SeasonData::toSeasonAverages)
                    var i = 0;
                    for (playerId in _datas) {
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
                            _seasondatas[i].pts * pesoPuntos +
                                    _seasondatas[i].blk * pesoTapones +
                                    _seasondatas[i].reb * pesoRebotes +
                                    _seasondatas[i].stl * pesoRobos +
                                    _seasondatas[i].ast * pesoAsistencias -
                                    _seasondatas[i].turnover * pesoErrores
                            )
                        db?.jugadorDao()?.insertar(jugador)
                        i=i+1;
                    }
                } catch (error: APIError) {
                    Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                }
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
                //Log.d("jug en bucle", "jug en bucle: $playerId")
                val seasonAverage = getNetworkService().getSeasonAverage(playerId).data
                //val puntos = seasonAverage.firstOrNull()?.pts ?: -1
                //Log.d("Season pts en bucle", "Season pts en bucle: $puntos")

                apiSeasonData.addAll(seasonAverage)
            } catch (e: Exception) {
                // Manejar la excepción genérica, imprime el mensaje de error
                Log.e("Error", "Error fetching season data: ${e.message}", e)
                throw APIError("Unable to fetch data from API", e)
            }
        }
        return apiSeasonData
    }
    private fun navegarPantallaPrincipal(usuario:Usuario, mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
        MainActivity.start(this,usuario)
    }
}