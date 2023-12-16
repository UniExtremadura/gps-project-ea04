package es.unex.nbafantasy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.data.UsuarioRepository

class MainViewModel(
    private val repositoryUsuario: UsuarioRepository
) : ViewModel(){

    private val _usuario = MutableLiveData<Usuario>(null)

    val usuario: LiveData<Usuario>
        get() = _usuario

    fun getUsuario(){
        userInSession=repositoryUsuario.usuario
    }

    //Guarda el valor en la variable Usuario
    var userInSession: Usuario? = null
        set(value) {
            field = value
            _usuario.value = value!!
        }

    private val _navigateToListajugador = MutableLiveData<Jugador>(null)
    private val _navigateToEditarjugador = MutableLiveData<Pair<Jugador?, Boolean>>(null)
    private val _navigateToResultado = MutableLiveData<ResultadoPartido>(null)

    val navigateListajugador: LiveData<Jugador>
        get() = _navigateToListajugador
    fun onShowClick(player: Jugador) {
        _navigateToListajugador.value = player
    }


    val navigateEditarjugador: LiveData<Pair<Jugador?, Boolean>>
        get() = _navigateToEditarjugador
    fun onEditClick(player: Jugador, Estrella: Boolean) {
        _navigateToEditarjugador.value = Pair(player, Estrella)
    }


    val navigateResultado: LiveData<ResultadoPartido>
        get() = _navigateToResultado
    fun onResultClick(player: ResultadoPartido) {
        _navigateToResultado.value = player
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

                return MainViewModel(
                    (application as NBAFantasyApplication).appContainer.repositoryUsuario
                ) as T
            }
        }
    }
}