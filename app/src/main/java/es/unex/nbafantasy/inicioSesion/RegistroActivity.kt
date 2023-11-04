package es.unex.nbafantasy.inicioSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        setUpListener()
    }

    private fun setUpListener(){
        val btnRegistro = findViewById<Button>(R.id.bt_registrarse)
        btnRegistro.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}