package es.unex.nbafantasy.inicioSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpListener()
    }

    private fun setUpListener(){
        val btnAcceso = findViewById<Button>(R.id.bt_acceso)
        btnAcceso.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnRegistro = findViewById<Button>(R.id.bt_registro)
        btnRegistro.setOnClickListener{
            val intent = Intent (this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}