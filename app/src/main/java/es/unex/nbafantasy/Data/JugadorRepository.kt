package es.unex.nbafantasy.Data

import androidx.lifecycle.LiveData
import com.example.example.SeasonData
import es.unex.nbafantasy.Data.api.Data
import es.unex.nbafantasy.Data.model.NBAData
import es.unex.nbafantasy.Data.model.NBASeasonData
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import kotlin.math.pow
import kotlin.math.roundToInt

class JugadorRepository private constructor(
    private val jugadorDao: JugadorDao,
    private val nbaFantasyApi: NBAFantasyApi
) {
    private var lastUpdateTimeMillis: Long = 0L
    private var pesoPuntos = 0.25
    private var pesoTapones = 0.12
    private var pesoRebotes = 0.12
    private var pesoRobos = 0.11
    private var pesoAsistencias = 0.2
    private var pesoErrores = 0.25

    val jugadores= jugadorDao.getAllJugadores()

    suspend fun getAll(): List<Jugador> {
        return jugadorDao.getAll()
    }

    suspend fun getJugadorById(jugadorId: Long):Jugador{
        return jugadorDao.getJugadorId(jugadorId)
    }
    fun getJugadoresByIds(ids: List<Long>): LiveData<List<Jugador>> {
        return jugadorDao.getJugadoresByIds(ids)
    }
    suspend fun tryUpdateRecentPlayersCache() {
        if (shouldUpdatePlayersCache()) {
            fetchRecentPlayers()
        }
    }

    suspend fun fetchRecentPlayers(): List<NBAData> {
        val apiData = mutableListOf<Data>()
        var newapi = emptyList<NBAData>()
        val playerIds = listOf(8, 4, 9, 12, 15, 18, 24, 28, 33, 37, 48, 53, 57, 79, 112,
            114, 115, 117, 125, 132, 140, 145, 175, 236, 237, 250, 246, 322, 434)
        try {
            for (playerId in playerIds) {
                val playerData = nbaFantasyApi.getPlayerById(playerId)
                apiData.add(playerData)
            }
            newapi = apiData.map {
                it.toNBAData()
            }
            //
            lastUpdateTimeMillis = System.currentTimeMillis()
        } catch (cause: Throwable) {
            throw APIError("No se puede obtener datos de la API", cause)
        }
        return newapi
    }
    suspend fun fetchSeason(Datos: List<NBAData>): List<SeasonData> {
        val apiSeasonData = mutableListOf<SeasonData>()

        for (i in Datos) {
            try {
                val playerId = i.id
                val seasonAverage = nbaFantasyApi.getSeasonAverage(playerId).data

                apiSeasonData.addAll(seasonAverage)
            } catch (e: Exception) {
                throw APIError("Unable to fetch data from API", e)
            }
        }
        return apiSeasonData
    }

    private suspend fun shouldUpdatePlayersCache(): Boolean {
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || jugadorDao.countJugadores() == 0
    }
    suspend fun ObtenerJugadores(_datas : List<NBAData>,_seasondatas: List<NBASeasonData>){
        var i = 0;
        val listaJug = mutableListOf<Jugador>()
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
            listaJug.add(jugador)
            //db?.jugadorDao()?.insertar(jugador)
            i=i+1;
            }
        jugadorDao.insertAll(listaJug)
    }
    fun Double.round(decimales: Int): Double {
        val factor = 10.0.pow(decimales.toDouble())
        return (this * factor).roundToInt() / factor
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000

        @Volatile
        private var INSTANCE: JugadorRepository? = null

        fun getInstance(jugadorDao: JugadorDao, nbaFantasyApi: NBAFantasyApi): JugadorRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: JugadorRepository(jugadorDao, nbaFantasyApi).also {
                    INSTANCE = it
                }
            }
        }
    }
}