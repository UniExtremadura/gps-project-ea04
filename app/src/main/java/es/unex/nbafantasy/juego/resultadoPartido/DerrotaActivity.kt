package es.unex.nbafantasy.juego.resultadoPartido

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.databinding.ActivityDerrotaBinding
import kotlinx.coroutines.launch

class DerrotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDerrotaBinding
    private val viewModel: DerrotaViewModel by viewModels { DerrotaViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_derrota)

        binding = ActivityDerrotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getUsuario()
            viewModel.getResultado()
            mostrarText()
            setUpListener()
        }
    }

    private fun mostrarText(){
        lifecycleScope.launch {
            with(binding) {
                val x = "Mis puntos: " + viewModel.Resultado!!.misPuntos.toString()
                misPuntos.setText(x)
                val y = "Puntos rival: " + viewModel.Resultado!!.puntosRivales.toString()
                puntosRival.setText(y)
            }
        }
    }

    private fun setUpListener(){
        val btnContinuar = findViewById<Button>(R.id.bt_salir_derrota)
        btnContinuar.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent )
        }
    }
}