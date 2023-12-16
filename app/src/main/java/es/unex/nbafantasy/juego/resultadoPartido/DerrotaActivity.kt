package es.unex.nbafantasy.juego.resultadoPartido

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.R
import es.unex.nbafantasy.bd.elemBD.ResultadoPartido
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.databinding.ActivityDerrotaBinding
import es.unex.nbafantasy.databinding.ActivityVictoriaBinding
import kotlinx.coroutines.launch

class DerrotaActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario
    private lateinit var resultadoPartido: ResultadoPartido
    private lateinit var binding: ActivityDerrotaBinding

    companion object {
        const val USUARIO = "USUARIO"
        const val RESULTADOPARTIDO="RESULTADOPARTIDO"

        fun start(
            context: Context,
            usuario: Usuario,
            resultadoPartido: ResultadoPartido
        ) {
            val intent = Intent(context, DerrotaActivity::class.java).apply {
                putExtra(USUARIO, usuario)
                putExtra(VictoriaActivity.RESULTADOPARTIDO, resultadoPartido)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_derrota)

        binding = ActivityDerrotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            usuario = (intent?.getSerializableExtra(USUARIO) as? Usuario)!!
            resultadoPartido=(intent?.getSerializableExtra(RESULTADOPARTIDO) as? ResultadoPartido)!!
            mostrarText()
            setUpListener()
        }
    }

    private fun mostrarText(){
        lifecycleScope.launch {
            with(binding) {
                val x = "Mis puntos: " + resultadoPartido.misPuntos.toString()
                misPuntos.setText(x)
                val y = "Puntos rival: " + resultadoPartido.puntosRivales.toString()
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