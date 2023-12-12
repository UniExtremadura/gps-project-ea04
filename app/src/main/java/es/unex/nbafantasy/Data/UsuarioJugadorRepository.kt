package es.unex.nbafantasy.Data

import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao
import es.unex.nbafantasy.bd.roomBD.UsuarioJugadorDao

class UsuarioJugadorRepository private constructor(
    private val usuarioJugadorDao: UsuarioJugadorDao) {


    companion object {
        @Volatile
        private var INSTANCE: UsuarioJugadorRepository? = null

        fun getInstance(usuarioJugadorDao: UsuarioJugadorDao): UsuarioJugadorRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UsuarioJugadorRepository(usuarioJugadorDao).also {
                    INSTANCE = it
                }
            }
        }
    }
}
