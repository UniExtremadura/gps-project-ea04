package es.unex.nbafantasy.home.resultado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.data.ResultadoPartidoRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.api.APIError
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ResultadoViewModel (
    private val repositoryResultadoPartido: ResultadoPartidoRepository
): ViewModel() {
    var usuario: Usuario? = null

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get()=_spinner

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get()=_toast

    init {
        refresh()
    }

    private fun refresh(){
        launchDataLoad { repositoryResultadoPartido }
    }

    fun onToastShown(){
        _toast.value=null
    }

    fun getResultado(): LiveData<List<ResultadoPartido>>{
        return repositoryResultadoPartido.getResultadoPartidoPorId(usuario!!.usuarioId!!)
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: APIError) {
                _toast.value= error.message
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
                val application = checkNotNull(extras
                    [ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return ResultadoViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryResultadoPartido,
                ) as T
            }
        }
    }
}