package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador

@Dao
interface ResultadoPartidoDao {
    /**
     * Devuelve todos los partidos de un usuario
     */
    @Query("SELECT * FROM resultadopartido WHERE usuarioId = :usuarioId")
    suspend fun getResultadoByUsuario(usuarioId: Long): List<ResultadoPartidoDao>

    /**
     * Devuelve un partido concreto de un usuario
     */
    @Query("SELECT * FROM resultadopartido WHERE usuarioId = :usuarioId and resultadoPartidoId = :resultadoPartidoId")
    suspend fun getResultado(usuarioId: Long, resultadoPartidoId: Long): ResultadoPartido

    /**
    * Inserta un elemento ResultadoPartido en la tabla.
    *
    * @param usuarioJugador
    */
    @Insert
    suspend fun insertar(resultadopartido: ResultadoPartido): Long

}