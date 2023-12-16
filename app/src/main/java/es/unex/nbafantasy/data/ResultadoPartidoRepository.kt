package es.unex.nbafantasy.data

import androidx.lifecycle.LiveData
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.ResultadoPartidoDao

class ResultadoPartidoRepository(
    private val resultadoPartidoDao: ResultadoPartidoDao) {

    var resultado : ResultadoPartido?=null

    suspend fun insertarResultado(resultadopartido: ResultadoPartido): Long{
        return resultadoPartidoDao.insertar(resultadopartido)
    }
    suspend fun setResultado(resultadoPartidoid: Long){
        resultado = resultadoPartidoDao.getResultado(resultadoPartidoid)
    }
    fun getResultadoPartidoPorId(usuarioId: Long): LiveData<List<ResultadoPartido>> {
        return resultadoPartidoDao.getResultadoByUsuario(usuarioId)
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
    }
}