package es.unex.nbafantasy.home.listaJugadores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.api.APIError
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListaJugadoresViewModel(
    private val jugadorRepository: JugadorRepository
): ViewModel() {
    val listaJugadores = jugadorRepository.jugadores
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
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras

                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return ListaJugadoresViewModel( (application as NBAFantasyApplication).appContainer.repositoryJugador,

                    ) as T
            }
        }
    }
}