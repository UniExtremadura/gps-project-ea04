package es.unex.nbafantasy.juego

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import es.unex.nbafantasy.R
import es.unex.nbafantasy.databinding.ActivityPantallaJuegoBinding
import es.unex.nbafantasy.juego.resultadoPartido.VictoriaActivity


class PantallaJuegoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPantallaJuegoBinding
    //private val PantallaJuegoViewModel: PantallaJuegoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListener()
    }

    private fun setUpListener(){
        val btnContinuar = findViewById<Button>(R.id.bt_comenzar_partida)
        btnContinuar.setOnClickListener{
            val intent = Intent (this, VictoriaActivity::class.java)
            //val intent = Intent (this, DerrotaActivity::class.java)
            startActivity(intent)
        }
    }
}