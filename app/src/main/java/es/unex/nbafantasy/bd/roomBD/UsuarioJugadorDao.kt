package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador

@Dao
interface UsuarioJugadorDao {
    /**
     * Devuelve todos los jugadores de un usuario dado su usuarioId
     *
     * @param usuario
     */
    @Query("SELECT * FROM usuariojugador WHERE usuarioId = :usuarioId")
    suspend fun mostrarJugadores(usuarioId: Int): Usuario

    /**
     * Inserta un usuario y un jugador en la tabla de usuarioJugador.
     *
     * @param usuarioId
     * @param jugadorId
     */
    @Insert
    suspend fun insertar(usuarioId: Long, jugadorId: Long): Long

    /**
     * Borra todos los un jugador de un usario concreto de la tabla de usuarioJugador.
     */
    @Query("DELETE FROM UsuarioJugador WHERE usuarioId = :usuarioId and jugadorId = :usuarioId")
    suspend fun deleteAll(usuarioId: Long, jugadorId: Long)

}