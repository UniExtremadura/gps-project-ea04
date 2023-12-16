package es.unex.nbafantasy.juego.resultadoPartido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.data.UsuarioJugadorRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import es.unex.nbafantasy.data.ResultadoPartidoRepository
import es.unex.nbafantasy.data.UsuarioRepository
import kotlinx.coroutines.launch

class VictoriaViewModel (
    private val repositoryJugador: JugadorRepository,
    private val repositoryUsuarioJugador: UsuarioJugadorRepository,
    private val repositoryUsuario: UsuarioRepository,
    private val resultadoPartido: ResultadoPartidoRepository,

): ViewModel(){
    var usuario: Usuario? = null
    var Resultado: ResultadoPartido? =null

    suspend fun getAllMisJugadores(): List<UsuarioJugador>{
       return repositoryUsuarioJugador.getAllMisJugadores(usuario!!.usuarioId!!)
    }
    fun getUsuario(){
        usuario = repositoryUsuario.usuario
    }
    fun getResultado(){
        Resultado = resultadoPartido.resultado
    }

    suspend fun getJugadorById(jugadorId: Long): Jugador {
        return repositoryJugador.getJugadorById(jugadorId)
    }

    suspend fun getUnUsuarioJugador(jugadorId: Long): UsuarioJugador{
        return repositoryUsuarioJugador.getUnUsuarioJugador(usuario!!.usuarioId!!,jugadorId)
    }

    fun insertarUsuarioJugador(usuarioJugador: UsuarioJugador){
        viewModelScope.launch {
            repositoryUsuarioJugador.insertarUsuarioJugador(usuarioJugador)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return VictoriaViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryJugador,
                    (application as NBAFantasyApplication).appContainer.repositoryUsuarioJugador,
                    (application as NBAFantasyApplication).appContainer.repositoryUsuario,
                    (application as NBAFantasyApplication).appContainer.repositoryResultadoPartido,
                ) as T
            }
        }
    }

}