package es.unex.nbafantasy.Data

import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao

class JugadorEquipoRepository private constructor(
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
        @Volatile
        private var INSTANCE: JugadorEquipoRepository? = null

        fun getInstance(jugadorEquipoDao: JugadorEquipoDao): JugadorEquipoRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: JugadorEquipoRepository(jugadorEquipoDao).also {
                    INSTANCE = it
                }
            }
        }
    }
}
