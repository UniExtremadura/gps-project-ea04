package es.unex.nbafantasy.utils

import android.content.Context
import es.unex.nbafantasy.data.JugadorEquipoRepository
import es.unex.nbafantasy.data.JugadorRepository
import es.unex.nbafantasy.data.ResultadoPartidoRepository
import es.unex.nbafantasy.data.UsuarioJugadorRepository
import es.unex.nbafantasy.data.UsuarioRepository
import es.unex.nbafantasy.api.getNetworkService
import es.unex.nbafantasy.bd.roomBD.BD

class AppContainer(context: Context?) {
    private val networkService = getNetworkService()
    private val db = BD.getInstance(context!!)
    val repositoryJugador = JugadorRepository(db!!.jugadorDao(),networkService)
    val repositoryJugadorEquipo = JugadorEquipoRepository(db!!.jugadorEquipoDao())
    val repositoryResultadoPartido = ResultadoPartidoRepository(db!!.resultadoPartidoDao())
    val repositoryUsuarioJugador = UsuarioJugadorRepository(db!!.usuarioJugadorDao(), repositoryJugador)
    val repositoryUsuario = UsuarioRepository(db!!.usuarioDao())
}