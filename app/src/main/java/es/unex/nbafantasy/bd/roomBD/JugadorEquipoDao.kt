package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador

@Dao
interface JugadorEquipoDao {
    /**
     * Devuelve la lista de jugadores de un Usuario
     *
     * @param usuarioId
     */
    @Query("SELECT * FROM jugadorequipo WHERE usuarioId = :usuarioId")
    suspend fun getJugadorByUsuario(usuarioId: Long): List<JugadorEquipo>


    /**
     * Inserta un usuario y un jugador en la tabla de usuarioJugador.
     *
     * @param jugadorEquipo
     */
    @Insert
    suspend fun insertar(jugadorEquipo: JugadorEquipo): Long

    /**
     * Borra todos los jugadores de un usario concreto de la tabla de usuarioJugador.
     */
    @Query("DELETE FROM UsuarioJugador WHERE usuarioId = :usuarioId")
    suspend fun deleteAll(usuarioId: Long)

}