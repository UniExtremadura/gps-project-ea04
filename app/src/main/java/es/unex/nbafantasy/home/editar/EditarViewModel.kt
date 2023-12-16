package es.unex.nbafantasy.home.editar

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.data.JugadorEquipoRepository
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.data.UsuarioJugadorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditarViewModel(
    private val jugadorRepository: JugadorRepository,
    private val usuarioJugadorRepository: UsuarioJugadorRepository,
    private val jugadorEquipoRepository: JugadorEquipoRepository
): ViewModel() {
    var usuario: Usuario? = null
    val listaJugadores = jugadorRepository.jugadores
    val usuarioJugador = usuarioJugadorRepository.usuarioJugador
    val jugadorEquipo = jugadorEquipoRepository.jugadoresEquipo

    private val _spinner = MutableLiveData<Boolean>()
    private val _toast = MutableLiveData<String?>()
    val spinner: LiveData<Boolean>
        get() = _spinner
    val toast: LiveData<String?>
        get() = _toast
    init{
        refresh()
    }
    private fun refresh(){
        launchDataLoad { jugadorRepository }
    }
    fun onToastShown() {
        _toast.value = null
    }
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value=true
                block()
            } catch (error: APIError) {
                _toast.value = error.message
            } finally {
                _spinner.value=false
            }
        }
    }
    fun getJugadoresDeUsuario(): LiveData<List<Jugador>> {
        return usuarioJugadorRepository.obtenerJugadoresDeUsuario(usuario!!.usuarioId!!)
    }
    fun getJugadoresEnEquipoDeUsuario(): LiveData<List<Jugador>> {
        return jugadorEquipoRepository.obtenerJugadoresDeUsuario(usuario!!.usuarioId!!)
    }
    suspend fun eliminarViewModel(jugadorId: Long){
        jugadorEquipoRepository.eliminar(usuario!!.usuarioId!!, jugadorId)
    }
    suspend fun insertarViewModel( jugadorId: Long): Long{
        return jugadorEquipoRepository.insertar(usuario!!.usuarioId!!,jugadorId)
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras

                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return EditarViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryJugador,
                    ((application as NBAFantasyApplication).appContainer.repositoryUsuarioJugador),
                    ((application as NBAFantasyApplication).appContainer.repositoryJugadorEquipo)
                ) as T
            }
        }
    }
}