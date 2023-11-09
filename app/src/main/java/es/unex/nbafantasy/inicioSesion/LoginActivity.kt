package es.unex.nbafantasy.inicioSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityLoginBinding
import es.unex.nbafantasy.utils.ComprobacionCredenciales
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: BD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializacion BD
        db= BD.getInstance(applicationContext)!!

        setUpListener()
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
                    val usuario = db?.usuarioDao()?.busquedaNombre(binding.etNombre.text.toString())
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

    private fun navegacionMainActivity(usuario: Usuario, mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
        MainActivity.start(this,usuario)
    }

    private fun navegacionRegistroActivity() {
        val btnRegistro = findViewById<Button>(R.id.bt_registro)
        btnRegistro.setOnClickListener{
            val intent = Intent (this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun notificarErrorCredencial(mensaje: String){
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
    }

}