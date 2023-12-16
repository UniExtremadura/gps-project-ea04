package es.unex.nbafantasy.inicioSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.data.UsuarioRepository
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.NBAFantasyApplication
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityLoginBinding
import es.unex.nbafantasy.perfil.PerfilViewModel
import es.unex.nbafantasy.utils.ComprobacionCredenciales
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: BD
    //private lateinit var repositoryUsuario: UsuarioRepository
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //val appContainer = (this.application as NBAFantasyApplication).appContainer
        //repositoryUsuario = appContainer.repositoryUsuario

        setUpListener()
        leerAutologin()
    }

    private fun leerAutologin(){
        val preferencias= PreferenceManager.getDefaultSharedPreferences(this).all

        val recordatorioDatos=preferencias["recordatorioDatos"] as Boolean? ?: false
        val nombreUsuario= preferencias["nombreUsuario"] as String? ?: ""
        val contrasena = preferencias["contrasena"] as String? ?: ""

        if(recordatorioDatos){
            binding.etNombre.setText(nombreUsuario)
            binding.etContrasena.setText(contrasena)
        }
    }

    private fun setUpListener(){
        with(binding) {
            btAcceso.setOnClickListener {
                checkLogin()
            }

            btRegistro.setOnClickListener{
                navegacionRegistroActivity()
            }
        }
    }

    private fun checkLogin(){
        with (binding){
            val comprobar = ComprobacionCredenciales.inicioSesion(
                binding.etNombre.text.toString(),
                binding.etContrasena.text.toString()
            )

            if (comprobar.fallo){
                notificarErrorCredencial(comprobar.msg)
            }else{
                lifecycleScope.launch{
                    val usuario = viewModel.busquedaNombre(binding.etNombre.text.toString())
                    if (usuario != null) {
                        val comprobarContra = ComprobacionCredenciales.compararContrasena(
                            binding.etContrasena.text.toString(),
                            usuario.contrasena
                        )

                        if (comprobarContra.fallo) {
                            notificarErrorCredencial(comprobarContra.msg)
                        }else{
                            navegacionMainActivity(usuario!!, comprobarContra.msg)
                        }

                    } else{
                        notificarErrorCredencial("Nombre de usuario invalido")
                    }
                }
            }
        }
    }

    //private suspend fun busquedaNombre(nombreUsuario:String): Usuario{
        //return repositoryUsuario.busquedaNombre(nombreUsuario)
    //}

    private suspend fun navegacionMainActivity(usuario: Usuario, mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
        viewModel.setUsuario(usuario.usuarioId!!)

        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent )
    }

    private fun navegacionRegistroActivity() {
        val intent = Intent (this, RegistroActivity::class.java)
        startActivity(intent)
    }

    private fun notificarErrorCredencial(mensaje: String){
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
    }

}