package es.unex.nbafantasy.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.data.UsuarioRepository

class PerfilViewModel  (
    private val repositoryUsuario: UsuarioRepository
): ViewModel() {
    var usuario: Usuario? = null

    suspend fun actualizarUsuario(nuevoNombre:String, nuevaContrasena:String){
        repositoryUsuario.actualizar(usuario!!.usuarioId!!,nuevoNombre,nuevaContrasena)
    }

    suspend fun busquedaNombre(nombreUsuario:String): Usuario{
        return repositoryUsuario.busquedaNombre(nombreUsuario)
    }

    suspend fun buscarIdByNombre(nombreUsuario:String): Int{
        return repositoryUsuario.buscarIdByNombre(nombreUsuario)
    }

    suspend fun buscarId():Usuario{
        return repositoryUsuario.buscarId(usuario!!.usuarioId!!)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras
                    [ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return PerfilViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryUsuario,
                ) as T
            }
        }
    }
}