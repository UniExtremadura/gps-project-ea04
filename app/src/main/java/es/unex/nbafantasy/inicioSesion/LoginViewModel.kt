package es.unex.nbafantasy.inicioSesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.data.UsuarioRepository
import es.unex.nbafantasy.perfil.PerfilViewModel

class LoginViewModel (
    private val repositoryUsuario: UsuarioRepository
): ViewModel() {

    suspend fun busquedaNombre(nombreUsuario:String): Usuario{
        return repositoryUsuario.busquedaNombre(nombreUsuario)
    }
    suspend fun setUsuario(usuarioId:Long){
        repositoryUsuario.setUsuario(usuarioId)
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

                return LoginViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryUsuario,
                ) as T
            }
        }
    }
}