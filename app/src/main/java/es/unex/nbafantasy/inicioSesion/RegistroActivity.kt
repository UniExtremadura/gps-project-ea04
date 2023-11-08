package es.unex.nbafantasy.inicioSesion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.ActivityMainBinding
import es.unex.nbafantasy.databinding.ActivityRegistroBinding
import es.unex.nbafantasy.utils.ComprobacionCredenciales
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {
    private lateinit var db: BD

    private lateinit var binding:ActivityRegistroBinding

    companion object{
        const val NOMBRE = "JOIN_NOMBRE"
        const val CONTRASENA = "JOIN_CONTRASENA"
        fun start(
            context: Context,
            responseLauncher: ActivityResultLauncher<Intent>
        ){
            val intent = Intent(context,RegistroActivity::class.java)
            responseLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializacion BD
        db= BD.getInstance(applicationContext)!!

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

                    if (db?.usuarioDao()?.busquedaNombre(binding.etNombre.text.toString())==null) {
                        val id = db?.usuarioDao()?.insertar(usuario)

                        navegarPantallaPrincipal(
                            Usuario(null, etNombre.text.toString(), etContrasena1.text.toString()),
                            comprobar.msg
                        )
                    }else{
                        notificarErrorCredencial("Nombre de usuario ocupado")
                    }
                }
            }
        }
    }

    private fun navegarPantallaPrincipal(usuario:Usuario, mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()

        MainActivity.start(this,usuario)
    }


    private fun notificarErrorCredencial(mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
    }
}