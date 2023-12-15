package es.unex.nbafantasy.data

import androidx.lifecycle.LiveData
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao

class JugadorEquipoRepository(
    private val jugadorEquipoDao: JugadorEquipoDao
) {
    val jugadoresEquipo = jugadorEquipoDao.getAllJugadorEquipo()

    suspend fun insertar(usuarioId: Long, jugadorId: Long): Long{
        val jugadorEquipo = JugadorEquipo(usuarioId, jugadorId)
        return jugadorEquipoDao.insertar(jugadorEquipo)
    }
    fun obtenerJugadoresDeUsuario(usuarioId: Long): LiveData<List<Jugador>> {
        return jugadorEquipoDao.getJugadoresByUsuariolive(usuarioId)
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
