package es.unex.nbafantasy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.unex.nbafantasy.R
import es.unex.nbafantasy.databinding.FragmentPantJuegoBinding
@AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        initNav()
    }

    private fun initNav() {
        binding.btJugar.setOnClickListener {
            findNavController().navigate(
                PantJuegoFragmentDirections.actionPantJuegoFragmentToPantallaJuegoActivity()
            )
        }
    }
}