package es.unex.nbafantasy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.databinding.ActivityMainBinding
import es.unex.nbafantasy.home.editar.EditarFragment
import es.unex.nbafantasy.home.editar.EditarFragmentDirections
import es.unex.nbafantasy.home.listaJugadores.ListaJugadoresFragment
import es.unex.nbafantasy.home.listaJugadores.ListaJugadoresFragmentDirections

import es.unex.nbafantasy.home.resultado.ResultadoFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ListaJugadoresFragment.OnJugadorClickListener, EditarFragment.OnEditarJugadorClickListener, ResultadoFragment.OnResultadoClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var usuario: Usuario

    companion object{
        const val USUARIO="USUARIO"

        fun start(
            context: Context,
            usuario: Usuario,
        ){
            val intent= Intent(context, MainActivity::class.java).apply {
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

        lifecycleScope.launch {
            usuario = (intent?.getSerializableExtra(USUARIO) as? Usuario)!!
            setUpUI()
        }
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
            if(destination.id==R.id.ajustesFragment||destination.id==R.id.listaJugadoresDetailsFragment
                || destination.id==R.id.perfilFragment||destination.id==R.id.editarDetailsFragment){
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

    override fun onShowClick(nbadata: Jugador) {
        val action = ListaJugadoresFragmentDirections.actionListaJugadoresFragmentToListaJugadoresDetailsFragment(
            nbadata)
        navController.navigate(action)
    }

    override fun onEditClick(data: Jugador, estrella: Boolean) {
        val action = EditarFragmentDirections.actionEditarFragmentToEditarDetailsFragment(
            data,estrella)
        navController.navigate(action)
    }

    override fun onResultClick(data: ResultadoPartido) {

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        lifecycleScope.launch {
            menuInflater.inflate(R.menu.menu_barra, menu)
            val searchItem = menu?.findItem(R.id.accion_ajustes)
            val searchView = searchItem?.actionView as? SearchView
        }
            return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.accion_perfil->{
            navController.navigate(HomeGrafonavDirections.acccionMainToPerfilFragment())
            true
        }R.id.accion_ajustes -> {
            navController.navigate(HomeGrafonavDirections.acccionMainToAjustesFragment())
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }

    }

    fun getUsuario(): Usuario? {
        return usuario
    }
}

