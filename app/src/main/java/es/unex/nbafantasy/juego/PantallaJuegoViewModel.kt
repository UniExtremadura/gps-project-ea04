package es.unex.nbafantasy.juego

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.Data.JugadorEquipoRepository
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.Data.ResultadoPartidoRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import kotlinx.coroutines.launch

class PantallaJuegoViewModel(
    private val repositoryResultadoPartido: ResultadoPartidoRepository,
    private val repositoryJugadorEquipo: JugadorEquipoRepository,
    private val repositoryJugador: JugadorRepository
): ViewModel() {
    var usuario: Usuario? = null

    suspend fun getJugadoresUsuario(): List<JugadorEquipo>{
        return repositoryJugadorEquipo.getJugadoresUsuario(usuario!!.usuarioId!!)
    }

    suspend fun getAll(): List<Jugador>{
        return repositoryJugador.getAll()
    }

    suspend fun getJugadorById(jugadorId: Long): Jugador{
        return repositoryJugador.getJugadorById(jugadorId)
    }

    fun insertarResultado(resultadoPartido: ResultadoPartido){
        viewModelScope.launch {
            repositoryResultadoPartido.insertarResultado((resultadoPartido))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return PantallaJuegoViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryResultadoPartido,
                    (application as NBAFantasyApplication).appContainer.repositoryJugadorEquipo,
                    (application as NBAFantasyApplication).appContainer.repositoryJugador
                ) as T
            }
        }
    }
}