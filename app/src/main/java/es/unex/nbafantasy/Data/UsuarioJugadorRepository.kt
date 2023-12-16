package es.unex.nbafantasy.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao
import es.unex.nbafantasy.bd.roomBD.UsuarioJugadorDao

class UsuarioJugadorRepository(
    private val usuarioJugadorDao: UsuarioJugadorDao,
    private val jugadorRepository: JugadorRepository ) {

    fun obtenerJugadoresDeUsuario(usuarioId: Long): LiveData<List<Jugador>> {
        return usuarioJugadorDao.getJugadorByUsuario(usuarioId).switchMap { listaUsuarioJugadores ->
            val idsJugadores = listaUsuarioJugadores.map { it.jugadorId }
            jugadorRepository.getJugadoresByIds(idsJugadores)
        }
    }
    suspend fun insertarUsuarioJugador(Usuario: UsuarioJugador){
        usuarioJugadorDao.insertar(Usuario)
    }

    suspend fun getAllMisJugadores(usuarioId: Long): List<UsuarioJugador> {
        return usuarioJugadorDao.getTodosJugadores(usuarioId)
    }

     suspend fun getUnUsuarioJugador(usuarioId: Long, jugadorId: Long): UsuarioJugador {
         return usuarioJugadorDao.getUnUsuarioJugador(usuarioId, jugadorId)
     }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
    }
}

