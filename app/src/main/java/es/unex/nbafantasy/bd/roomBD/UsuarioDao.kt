package es.unex.nbafantasy.bd.roomBD

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.nbafantasy.bd.elemBD.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario WHERE nombre LIKE :nombreUsuario LIMIT 1")
    suspend fun busquedaNombre(nombreUsuario: String): Usuario

    @Insert
    suspend fun insertar(usuario: Usuario): Long
}