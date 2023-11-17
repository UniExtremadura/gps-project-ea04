package es.unex.nbafantasy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object{
        const val USUARIO="USUARIO"

        fun start(
            context: Context,
            usuario: Usuario,
        ){
            val intent=Intent(context, MainActivity::class.java).apply {
                putExtra(USUARIO, usuario)
            }
            context.startActivity(intent)
        }
    }

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

        navController.addOnDestinationChangedListener{_,destination,_ ->
            if(destination.id==R.id.ajustesFragment){
                binding.toolbar.menu.clear()
                binding.bottomNavigation.visibility= View.GONE
            }else{
                binding.toolbar.visibility=View.VISIBLE
                binding.bottomNavigation.visibility=View.VISIBLE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_barra, menu)
        val searchItem = menu?.findItem(R.id.accion_ajustes)
        val searchView = searchItem?.actionView as? SearchView

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.accion_ajustes -> {
            val accion= HomeGrafonavDirections.acccionMainToAjustesFragment()
            navController.navigate(accion)
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }

    }
}