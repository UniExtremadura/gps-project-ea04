package es.unex.nbafantasy.Data

import es.unex.nbafantasy.api.NBAFantasyApi
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.bd.roomBD.JugadorDao
import es.unex.nbafantasy.bd.roomBD.JugadorEquipoDao
import es.unex.nbafantasy.bd.roomBD.UsuarioDao
import es.unex.nbafantasy.bd.roomBD.UsuarioJugadorDao

class UsuarioRepository private constructor(
    private val usuarioDao: UsuarioDao) {

    suspend fun busquedaNombre(nombreUsuario: String): Usuario {
        return usuarioDao.busquedaNombre(nombreUsuario)
    }

    suspend fun insertar(usuario: Usuario):Long{
        return usuarioDao.insertar(usuario)
    }

    suspend fun actualizar(usuarioId:Long, nuevoNombre:String, nuevaContrasena:String){
        usuarioDao.actualizarUsuario(usuarioId, nuevoNombre, nuevaContrasena)
    }

    suspend fun buscarIdByNombre(nombreUsuario: String): Int{
        return usuarioDao.buscarIdByNombre(nombreUsuario)
    }

    companion object {
        @Volatile
        private var INSTANCE: UsuarioRepository? = null

        fun getInstance(usuarioDao: UsuarioDao): UsuarioRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UsuarioRepository(usuarioDao).also {
                    INSTANCE = it
                }
            }
        }
    }
}
