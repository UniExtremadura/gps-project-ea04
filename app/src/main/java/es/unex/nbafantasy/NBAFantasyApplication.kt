package es.unex.nbafantasy

import android.app.Application
import es.unex.nbafantasy.utils.AppContainer

class NBAFantasyApplication : Application(){
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}