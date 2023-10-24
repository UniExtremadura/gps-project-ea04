package es.unex.nbafantasy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import es.unex.nbafantasy.R
import es.unex.nbafantasy.databinding.FragmentPantJuegoBinding

class PantJuegoFragment : Fragment() {

    private lateinit var botonJuego: Button
    private var _binding:FragmentPantJuegoBinding? = null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentPantJuegoBinding.inflate(layoutInflater,container, false)
        return binding.root
    }
}