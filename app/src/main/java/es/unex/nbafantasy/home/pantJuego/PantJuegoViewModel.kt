import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.Data.JugadorEquipoRepository
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.JugadorEquipo
import es.unex.nbafantasy.bd.elemBD.Usuario

class PantJuegoViewModel(
    private val repositoryJugadorEquipo: JugadorEquipoRepository,
    private val repositoryJugador: JugadorRepository
) : ViewModel() {
    var usuario: Usuario?=null
    var jugador = repositoryJugador.jugadores
    var jugadorEquipo = repositoryJugadorEquipo.jugadoresEquipo


    suspend fun obtenerListaJugadores(): List<JugadorEquipo> {
        return repositoryJugadorEquipo.getJugadoresUsuario(usuario!!.usuarioId!!)
    }

    suspend fun getJugadorById(jugadorId: Long): Jugador{
        return repositoryJugador.getJugadorById(jugadorId)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return PantJuegoViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryJugadorEquipo,
                    (application as NBAFantasyApplication).appContainer.repositoryJugador,
                ) as T
            }
        }
    }
}