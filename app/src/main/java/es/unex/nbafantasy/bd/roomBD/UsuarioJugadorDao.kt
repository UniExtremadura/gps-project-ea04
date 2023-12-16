package es.unex.nbafantasy.bd.roomBD

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador

@Dao
interface UsuarioJugadorDao {
    /**
     * Recupera todas las relaciones entre usuarios y jugadores de la base de datos.
     *
     * @return LiveData conteniendo una lista de todas las relaciones 'UsuarioJugador'.
     */
    @Query("SELECT * FROM UsuarioJugador")
    fun getAllJugUsuarios(): LiveData<List<UsuarioJugador>>
    /**
     * Obtiene todos los jugadores asociados a un usuario específico.
     *
     * @param usuarioId El identificador único del usuario.
     * @return Una lista de objetos [UsuarioJugador] que pertenecen al usuario especificado.
     */
    @Query("SELECT * FROM usuariojugador WHERE usuarioId = :usuarioId")
    suspend fun getTodosJugadores(usuarioId: Long): List<UsuarioJugador>

    /**
     * Obtiene la lista de relaciones entre usuarios y jugadores para un usuario específico.
     *
     * @param usuarioId El identificador del usuario para el cual se buscan las relaciones con jugadores.
     * @return LiveData conteniendo una lista de objetos 'UsuarioJugador' que representan
     *         la relación entre el usuario especificado y sus jugadores.
     */
    @Query("SELECT * FROM usuariojugador WHERE usuarioId = :usuarioId")
    fun getJugadorByUsuario(usuarioId: Long): LiveData<List<UsuarioJugador>>

    /**
     * Obtiene una asociación específica de usuario y jugador por sus identificadores únicos.
     *
     * @param usuarioId El identificador único del usuario.
     * @param jugadorId El identificador único del jugador.
     * @return Un objeto [UsuarioJugador] que corresponde a la asociación de usuario y jugador especificada.
     */
    @Query("SELECT * FROM usuariojugador WHERE usuarioId = :usuarioId and jugadorId = :jugadorId")
    suspend fun getUnUsuarioJugador(usuarioId: Long, jugadorId: Long): UsuarioJugador

    /**
     * Inserta una asociación de usuario y jugador en la tabla de usuarioJugador.
     *
     * @param usuarioJugador El objeto [UsuarioJugador] que representa la relación entre un usuario y un jugador.
     * @return El identificador único de la asociación recién insertada.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(usuarioJugador: UsuarioJugador): Long

    /**
     * Borra todas las asociaciones de un usuario con jugadores de la tabla de usuarioJugador.
     *
     * @param usuarioId El identificador único del usuario.
     */
    @Query("DELETE FROM UsuarioJugador WHERE usuarioId = :usuarioId")
    suspend fun deleteAll(usuarioId: Long)

    /**
     * Obtiene una lista de todas las asociaciones de usuario y jugador en la base de datos.
     *
     * @return Una lista de objetos [UsuarioJugador].
     */
    @Query("SELECT * FROM UsuarioJugador")
    suspend fun getAll(): List<UsuarioJugador>

}