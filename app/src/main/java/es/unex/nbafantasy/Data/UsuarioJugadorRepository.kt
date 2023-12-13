package es.unex.nbafantasy.Data

import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao
import es.unex.nbafantasy.bd.roomBD.UsuarioJugadorDao

class UsuarioJugadorRepository private constructor(
    private val usuarioJugadorDao: UsuarioJugadorDao,
    private val jugadorRepository: JugadorRepository ) {

    suspend fun getAllMisJugadores(usuarioId: Long): List<UsuarioJugador> {
        return usuarioJugadorDao.getTodosJugadores(usuarioId)
    }

    suspend fun getUnUsuarioJugador(usuarioId: Long, jugadorId: Long):UsuarioJugador{
        return usuarioJugadorDao.getUnUsuarioJugador(usuarioId, jugadorId)
    }

    suspend fun insertarUsuarioJugador(usuarioJugador: UsuarioJugador):Long{
        return usuarioJugadorDao.insertar(usuarioJugador)
    }

    companion object {
        @Volatile
        private var INSTANCE: UsuarioJugadorRepository? = null

        fun getInstance(usuarioJugadorDao: UsuarioJugadorDao, jugadorRepository: JugadorRepository): UsuarioJugadorRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UsuarioJugadorRepository(usuarioJugadorDao, jugadorRepository).also {
                    INSTANCE = it
                }
            }
        }
    }
}
