package es.unex.nbafantasy.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao
import es.unex.nbafantasy.bd.roomBD.ResultadoPartidoDao

class ResultadoPartidoRepository(
    private val resultadoPartidoDao: ResultadoPartidoDao) {

    val resultados= resultadoPartidoDao.getAllResultados()

    suspend fun insertarResultado(resultadopartido: ResultadoPartido){
        resultadoPartidoDao.insertar(resultadopartido)
    }

    fun getResultadoPartidoPorId(usuarioId: Long): LiveData<List<ResultadoPartido>> {
        return resultadoPartidoDao.getResultadoByUsuario(usuarioId)
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
    }
}