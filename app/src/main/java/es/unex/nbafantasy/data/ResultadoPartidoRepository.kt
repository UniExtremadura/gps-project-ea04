package es.unex.nbafantasy.data

import androidx.lifecycle.LiveData
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
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