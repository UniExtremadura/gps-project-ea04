package es.unex.nbafantasy.Data

import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao
import es.unex.nbafantasy.bd.roomBD.ResultadoPartidoDao

class ResultadoPartidoRepository private constructor(
    private val resultadoPartidoDao: ResultadoPartidoDao) {

    val resultados= resultadoPartidoDao.getAllResultados()

    suspend fun insertarResultado(resultadopartido: ResultadoPartido){
        resultadoPartidoDao.insertar(resultadopartido)
    }

    companion object {
        @Volatile
        private var INSTANCE: ResultadoPartidoRepository? = null

        fun getInstance(resultadoPartidoDao: ResultadoPartidoDao): ResultadoPartidoRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ResultadoPartidoRepository(resultadoPartidoDao).also {
                    INSTANCE = it
                }
            }
        }
    }
}
