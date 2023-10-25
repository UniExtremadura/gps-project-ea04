package es.unex.nbafantasy.Juego

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import es.unex.nbafantasy.R
import es.unex.nbafantasy.databinding.ActivityMainBinding
import es.unex.nbafantasy.databinding.ActivityPantallaJuegoBinding

@AndroidEntryPoint
class PantallaJuegoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPantallaJuegoBinding

    private val PantallaJuegoViewModel:PantallaJuegoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}