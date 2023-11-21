package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador

@Dao
interface UsuarioJugadorDao {
    /**
     * Devuelve todos los jugadores de un usuario concreto
     */
    @Query("SELECT * FROM usuariojugador WHERE usuarioId = :usuarioId")
    suspend fun getJugadorByUsuario(usuarioId: Int): List<UsuarioJugador>


    /**
     * Inserta un usuario y un jugador en la tabla de usuarioJugador.
     *
     * @param usuarioJugador
     */
    @Insert
    suspend fun insertar(usuarioJugador: UsuarioJugador): Long

    /**
     * Borra todos los jugadores de un usuario concreto de la tabla de usuarioJugador.
     */
    @Query("DELETE FROM UsuarioJugador WHERE usuarioId = :usuarioId")
    suspend fun deleteAll(usuarioId: Long)

}