package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.Usuario

@Dao
interface JugadorDao {
    @Query("SELECT * FROM jugador WHERE nombre LIKE :nombreJugador LIMIT 1")
    suspend fun busquedaNombre(nombreJugador: String): Jugador

    /**
     * Devuelve un Jugador dado su jugadorId.
     *
     * @param jugadorId
     */

    @Query("SELECT * FROM jugador WHERE jugadorId= :jugadorId")
    suspend fun getJugadorId(jugadorId: Long): Jugador

    /**
     * Devuelve una lista de jugadores
     */
    @Query("SELECT * FROM jugador")
    suspend fun getAll(): List<Jugador>

    /**
     * Inserta un jugador en la tabla de jugador.
     *
     * @param jugador
     */
    @Insert
    suspend fun insertar(jugador: Jugador): Long

    /**
     * Cuenta el número de jugadores en la tabla de jugador.
     */
    @Query("SELECT COUNT(*) FROM jugador")
    suspend fun countJugadores(): Int

    /**
     * Borra todos los Jugadores de la tabla de jugador.
     */
    @Query("DELETE FROM jugador")
    suspend fun deleteAll()
}