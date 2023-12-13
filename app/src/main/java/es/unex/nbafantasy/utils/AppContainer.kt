package es.unex.nbafantasy.utils

import android.content.Context
import es.unex.nbafantasy.Data.JugadorEquipoRepository
import es.unex.nbafantasy.Data.JugadorRepository
import es.unex.nbafantasy.Data.ResultadoPartidoRepository
import es.unex.nbafantasy.Data.UsuarioJugadorRepository
import es.unex.nbafantasy.Data.UsuarioRepository
import es.unex.nbafantasy.api.NBAFantasyApi
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