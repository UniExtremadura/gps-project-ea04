package es.unex.nbafantasy.juego.resultadoPartido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.data.ResultadoPartidoRepository
import es.unex.nbafantasy.data.UsuarioRepository

class DerrotaViewModel(
    private val repositoryUsuario: UsuarioRepository,
    private val resultadoPartido: ResultadoPartidoRepository
): ViewModel(){
    var usuario: Usuario? = null
    var Resultado: ResultadoPartido? =null
    fun getUsuario(){
        usuario=repositoryUsuario.usuario
    }
    fun getResultado(){
        Resultado = resultadoPartido.resultado
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return DerrotaViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryUsuario,
                    (application as NBAFantasyApplication).appContainer.repositoryResultadoPartido,
                ) as T
            }
        }
    }

}