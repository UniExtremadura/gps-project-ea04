package es.unex.nbafantasy.bd.roomBD

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.Jugador

@Dao
interface JugadorDao {
    /**
     * Recupera todos los jugadores registrados en la base de datos.
     *
     * @return LiveData conteniendo una lista de todos los jugadores.
     */
    @Query("SELECT * FROM jugador")
    fun getAllJugadores(): LiveData<List<Jugador>>

    /**
     * Busca y retorna jugadores específicos basados en una lista de identificadores.
     *
     * @param ids Una lista de identificadores de jugadores que se desean recuperar.
     * @return LiveData con la lista de jugadores que coinciden con los identificadores dados.
     */
    @Query("SELECT * FROM Jugador WHERE jugadorId IN (:ids)")
    fun getJugadoresByIds(ids: List<Long>): LiveData<List<Jugador>>

    /**
     * Inserta una lista de jugadores en la base de datos.
     *
     * @param jugadores La lista de jugadores a insertar en la base de datos.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(Jugador: List<Jugador>)

    /**
     * Realiza una búsqueda de un jugador por su nombre.
     *
     * @param nombreJugador El nombre del jugador a buscar.
     * @return Un objeto [Jugador] que coincide con el nombre proporcionado, o null si no se encuentra.
     */
    @Query("SELECT * FROM jugador WHERE nombre LIKE :nombreJugador LIMIT 1")
    suspend fun busquedaNombre(nombreJugador: String): Jugador

    /**
     * Obtiene un jugador por su identificador único.
     *
     * @param jugadorId El identificador único del jugador.
     * @return Un objeto [Jugador] que corresponde al identificador proporcionado.
     */
    @Query("SELECT * FROM jugador WHERE jugadorId= :jugadorId")
    suspend fun getJugadorId(jugadorId: Long): Jugador

    /**
     * Obtiene una lista de todos los jugadores en la base de datos.
     *
     * @return Una lista de objetos [Jugador].
     */
    @Query("SELECT * FROM jugador")
    suspend fun getAll(): List<Jugador>

    /**
     * Inserta un nuevo jugador en la tabla de jugadores.
     *
     * @param jugador El objeto [Jugador] a insertar.
     * @return El identificador único del jugador recién insertado.
     */
    @Insert
    suspend fun insertar(jugador: Jugador): Long

    /**
     * Obtiene el número total de jugadores en la base de datos.
     *
     * @return El número total de jugadores.
     */
    @Query("SELECT COUNT(*) FROM jugador")
    suspend fun countJugadores(): Int

    /**
     * Elimina todos los jugadores de la tabla de jugadores.
     */
    @Query("DELETE FROM jugador")
    suspend fun deleteAll()
}