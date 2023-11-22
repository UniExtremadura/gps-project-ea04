package es.unex.nbafantasy.home
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.unex.nbafantasy.bd.elemBD.Jugador

class EstrellaViewModel: ViewModel() {
    val jugadoresSeleccionados = MutableLiveData<List<Jugador>>()
}