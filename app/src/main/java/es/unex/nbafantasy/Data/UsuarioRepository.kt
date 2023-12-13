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

class UsuarioRepository (
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

    suspend fun buscarId(usuarioId: Long): Usuario{
        return usuarioDao.getUsuarioId(usuarioId.toInt())
    }
    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
    }
}
