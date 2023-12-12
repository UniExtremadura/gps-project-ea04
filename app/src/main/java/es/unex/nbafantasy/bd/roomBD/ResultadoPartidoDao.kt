package es.unex.nbafantasy.bd.roomBD

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador

@Dao
interface ResultadoPartidoDao {
    /**
     * Obtiene todos los resultados de partidos asociados a un usuario específico.
     *
     * @param usuarioId El identificador único del usuario.
     * @return Una lista de objetos [ResultadoPartido] que pertenecen al usuario especificado.
     */
    @Query("SELECT * FROM resultadopartido WHERE usuarioId = :usuarioId")
    suspend fun getResultadoByUsuario(usuarioId: Long): List<ResultadoPartido>

    @Query("SELECT * FROM ResultadoPartido")
    fun getAllResultados(): LiveData<List<ResultadoPartido>>

    /**
     * Obtiene un resultado de partido específico por su identificador único.
     *
     * @param resultadoPartidoId El identificador único del resultado del partido.
     * @return Un objeto [ResultadoPartido] que corresponde al identificador proporcionado.
     */
    @Query("SELECT * FROM resultadopartido WHERE resultadoPartidoId = :resultadoPartidoId")
    suspend fun getResultado(resultadoPartidoId: Long): ResultadoPartido

    /**
     * Inserta un nuevo resultado de partido en la tabla.
     *
     * @param resultadopartido El objeto [ResultadoPartido] a insertar.
     * @return El identificador único del resultado de partido recién insertado.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(resultadopartido: ResultadoPartido)

}