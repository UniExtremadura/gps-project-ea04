package es.unex.nbafantasy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import es.unex.nbafantasy.databinding.ActivityMainBinding
import es.unex.nbafantasy.home.EditarFragment
import es.unex.nbafantasy.home.ListaJugadoresFragment
import es.unex.nbafantasy.home.PantJuegoFragment
import es.unex.nbafantasy.home.ResultadoFragment
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var editarFragment: EditarFragment
    private lateinit var listaJugadoresFragment: ListaJugadoresFragment
    private lateinit var pantJuegoFragment: PantJuegoFragment
    private lateinit var resultadoFragment: ResultadoFragment

    val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
    }

    fun setUpUI() {
        /*//Inicializacion de cada Fragment
        discoverFragment = DiscoverFragment()
        libraryFragment = LibraryFragment()
        userFragment = UserFragment()

        //FRAGMENTO INICIAL DEFINIDO que va a ser discoverFragment, es decir cuando se arranque la
        // aplicacion y se vea la ventana HOME se vera este fragmento
        //setCurrentFragment(discoverFragment)

        // Obtenemos el navHostFragment a partir del fragmetManager
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        //A la barra de bnavegacion inferior lo configuramos con el NavController
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)*/

        binding.bottomNavigation.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.editarFragment,
                R.id.listaJugadoresFragment,
                R.id.pantJuegoFragment,
                R.id.resultadoFragment)
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}