package es.unex.nbafantasy.juego.resultadoPartido

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R

class VictoriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victoria)

        setUpListener()
    }

    private fun setUpListener(){
        val btnContinuar = findViewById<Button>(R.id.bt_salir_victoria)
        btnContinuar.setOnClickListener{
            val intent = Intent (this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}