package es.unex.nbafantasy.inicioSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.data.UsuarioRepository
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityRegistroBinding
import es.unex.nbafantasy.juego.DarCartaActivity
import es.unex.nbafantasy.juego.DarCartaViewModel
import es.unex.nbafantasy.perfil.PerfilViewModel
import es.unex.nbafantasy.utils.ComprobacionCredenciales
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {
    private lateinit var db: BD
    private lateinit var binding:ActivityRegistroBinding
    //private lateinit var repositoryUsuario: UsuarioRepository
    private val viewModel: RegistroViewModel by viewModels { RegistroViewModel.Factory }
    private val darCartaViewModel: DarCartaViewModel by viewModels { DarCartaViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListener()
    }

    private fun setUpListener() {
        with(binding) {
            btRegistrarse.setOnClickListener {
                crearCuenta()
            }
        }
    }

    private fun crearCuenta() {
        with(binding) {
            val comprobar = ComprobacionCredenciales.registro(
                etNombre.text.toString(),
                etContrasena1.text.toString(),
                etContrasena2.text.toString()
            )

            if (comprobar.fallo) { //Cuando las credenciales son erroneas
                notificarErrorCredencial(comprobar.msg)
            } else {
                lifecycleScope.launch {
                    val usuario = Usuario(
                        null,
                        etNombre.text.toString(),
                        etContrasena1.text.toString()
                    )

                    if (viewModel.busquedaNombre(binding.etNombre.text.toString())==null) {
                        val id = viewModel.insertarUsuario(usuario)
                        val usuario = Usuario(null, etNombre.text.toString(), etContrasena1.text.toString())

                        usuario.usuarioId=id

                        navegarPantallaPrincipal(usuario,comprobar.msg)
                    }else{
                        notificarErrorCredencial("Nombre de usuario ocupado")
                    }
                }
            }
        }
    }

    private suspend fun navegarPantallaPrincipal(usuario:Usuario, mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
        darCartaViewModel.setUsuario(usuario.usuarioId!!)

        val intent = Intent (this, DarCartaActivity::class.java)
        startActivity(intent )
    }

    private fun notificarErrorCredencial(mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
    }
}