package es.unex.nbafantasy.data

import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.UsuarioDao

class UsuarioRepository (
    private val usuarioDao: UsuarioDao) {

    var usuario : Usuario?=null

    suspend fun setUsuario(usuarioId: Long){
        usuario=usuarioDao.getUsuarioId(usuarioId.toInt())
    }

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
