package es.unex.nbafantasy.juego

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.example.SeasonData
import es.unex.nbafantasy.data.JugadorEquipoRepository
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.data.UsuarioJugadorRepository
import es.unex.nbafantasy.data.model.NBAData
import es.unex.nbafantasy.data.model.NBASeasonData
import es.unex.nbafantasy.data.toSeasonAverages
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.elemBD.UsuarioJugador
import kotlinx.coroutines.launch

class DarCartaViewModel(
    private val repositoryJugador: JugadorRepository,
    private val repositoryUsuarioJugador: UsuarioJugadorRepository,
    private val repositoryJugadorEquipo: JugadorEquipoRepository
): ViewModel() {
    var usuario: Usuario? = null

    suspend fun getAll(): List<Jugador>{
        return repositoryJugador.getAll()
    }

    fun insertarUsuarioJugador(usuarioJugador: UsuarioJugador){
        viewModelScope.launch {
            repositoryUsuarioJugador.insertarUsuarioJugador(usuarioJugador)
        }
    }

    fun insertarJugadorEquipo (posicionJugador: Int){
        viewModelScope.launch {
            repositoryJugadorEquipo.insertar(usuario!!.usuarioId!!, posicionJugador.toLong())
        }
    }

    suspend fun getJugadorById(jugadorId: Long): Jugador{
        return repositoryJugador.getJugadorById(jugadorId)
    }

    suspend fun cargaDatos(): List<NBAData>{
        return repositoryJugador.fetchRecentPlayers()
    }

    suspend fun cargaEstadisticas(_datas: List<NBAData>): List<NBASeasonData>{
        return repositoryJugador.fetchSeason(_datas).map(SeasonData::toSeasonAverages)
    }

    suspend fun carga(_datas: List<NBAData>, _seasondatas: List<NBASeasonData>){
        repositoryJugador.ObtenerJugadores(_datas,_seasondatas)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return DarCartaViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryJugador,
                    (application as NBAFantasyApplication).appContainer.repositoryUsuarioJugador,
                    (application as NBAFantasyApplication).appContainer.repositoryJugadorEquipo
                ) as T
            }
        }
    }
}