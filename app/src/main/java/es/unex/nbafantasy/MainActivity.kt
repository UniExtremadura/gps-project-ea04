package es.unex.nbafantasy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import es.unex.nbafantasy.databinding.ActivityMainBinding
import es.unex.nbafantasy.home.EditarFragment
import es.unex.nbafantasy.home.ListaJugadoresFragment
import es.unex.nbafantasy.home.PantJuegoFragment
import es.unex.nbafantasy.home.ResultadoFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController:NavController
    /*private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()
    }

    private fun setUpUi(){
        initNavigation()
    /*  binding.bottomNavigation.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
                setOf( R.id.editarFragment,
                    R.id.pantJuegoFragment,
                    R.id.listaJugadoresFragment,
                    R.id.resultadoFragment)
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
*/
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun setUpListeners(){


    }


}