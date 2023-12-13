package es.unex.nbafantasy.Data

import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao

class JugadorEquipoRepository(
    private val jugadorEquipoDao: JugadorEquipoDao) {
    val jugadoresEquipo = jugadorEquipoDao.getAllJugadorEquipo()

    suspend fun insertar(usuarioId: Long, jugadorId: Long): Long{
        val jugadorEquipo = JugadorEquipo(usuarioId, jugadorId)
        return jugadorEquipoDao.insertar(jugadorEquipo)
    }

    suspend fun getJugadoresUsuario(usuarioId: Long):List<JugadorEquipo>{
        return jugadorEquipoDao.getJugadoresByUsuario(usuarioId)
    }

    suspend fun eliminar(usuarioId: Long, jugadorId: Long){
        jugadorEquipoDao.eliminar(usuarioId, jugadorId)
    }


    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
    }
}
