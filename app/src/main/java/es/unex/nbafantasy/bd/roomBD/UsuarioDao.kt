package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.unex.nbafantasy.bd.elemBD.Usuario

@Dao
interface UsuarioDao {
    /**
     * Busca un Usuario por su nombre.
     *
     * @param nombreUsuario El nombre del usuario a buscar.
     * @return Un objeto [Usuario] si se encuentra, o nulo si no hay coincidencias.
     */
    @Query("SELECT * FROM usuario WHERE nombre LIKE :nombreUsuario LIMIT 1")
    suspend fun busquedaNombre(nombreUsuario: String): Usuario

    /**
     * Busca el ID de un Usuario por su nombre.
     *
     * @param nombreUsuario El nombre del usuario para el cual buscar el ID.
     * @return El ID del usuario si se encuentra, o -1 si no hay coincidencias.
     */
    @Query("SELECT usuarioId FROM usuario WHERE nombre LIKE :nombreUsuario LIMIT 1")
    suspend fun buscarIdByNombre(nombreUsuario: String): Int

    /**
     * Obtiene un Usuario por su ID.
     *
     * @param usuarioId El ID del usuario a obtener.
     * @return Un objeto [Usuario] si se encuentra, o nulo si no hay coincidencias.
     */
    @Query("SELECT * FROM usuario WHERE usuarioId= :usuarioId")
    suspend fun getUsuarioId(usuarioId: Int): Usuario

    /**
     * Obtiene un Usuario por su nombre de usuario y contraseña.
     *
     * @param nombre El nombre de usuario.
     * @param contrasena La contraseña del usuario.
     * @return Un objeto [Usuario] si las credenciales son válidas, o nulo si no hay coincidencias.
     */
    @Query ("SELECT * FROM usuario WHERE nombre = :nombre and contrasena = :contrasena")
    suspend fun getByLogin(nombre: String, contrasena: String): Usuario

    /**
     * Inserta un nuevo usuario en la tabla.
     *
     * @param usuario El objeto Usuario a insertar.
     * @return El ID del nuevo usuario insertado.
     */
    @Insert
    suspend fun insertar(usuario: Usuario): Long

    /**
     * Actualiza el nombre y la contraseña de un Usuario dado su ID.
     *
     * @param usuarioId El ID del usuario a actualizar.
     * @param nuevoNombre El nuevo nombre para el usuario.
     * @param nuevaContrasena La nueva contraseña para el usuario.
     */
    @Query("UPDATE usuario SET nombre = :nuevoNombre, contrasena = :nuevaContrasena WHERE usuarioId = :usuarioId")
    suspend fun actualizarUsuario(usuarioId: Long, nuevoNombre: String, nuevaContrasena: String)

    /**
     * Actualiza un Usuario en la tabla.
     *
     * @param usuario El objeto [Usuario] a actualizar.
     */
    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    /**
     * Elimina todos los usuarios de la tabla.
     */
    @Query("DELETE FROM usuario")
    suspend fun deleteAll()
}