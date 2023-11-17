package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.unex.nbafantasy.bd.elemBD.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario WHERE nombre LIKE :nombreUsuario LIMIT 1")
    suspend fun busquedaNombre(nombreUsuario: String): Usuario

    /**
     * Devuelve un Usuario dado su userId.
     *
     * @param usuario
     */
    @Query("SELECT * FROM usuario WHERE userId= :userId")
    suspend fun getUsuarioId(userId: Int): Usuario

    /**
     * Devuelve un Usuario dado su nombre de usuario y contraseña.
     *
     * @param nombre
     * @param contrasena
     */
    @Query ("SELECT * FROM usuario WHERE nombre = :nombre")
    suspend fun getByNombreUsuario(nombre: String): Usuario

    /**
     * Devuelve un Usuario dado su nombre de usuario y contraseña.
     *
     * @param nombre
     * @param contrasena
     */
    @Query ("SELECT * FROM usuario WHERE nombre = :nombre and contrasena = :contrasena")
    suspend fun getByLogin(nombre: String, contrasena: String): Usuario

    /**
     * Inserta un usario en la tabla de usuario.
     *
     * @param usuario
     */
    @Insert
    suspend fun insertar(usuario: Usuario): Long

    /**
     * Actualiza un Usuario de la tabla de usuario.
     *
     * @param usuario
     */
    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    /**
     * Borra todos los Usuarios de la tabla de usuarios.
     */
    @Query("DELETE FROM usuario")
    suspend fun deleteAll()
}