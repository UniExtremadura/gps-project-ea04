package es.unex.nbafantasy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.unex.nbafantasy.R
import es.unex.nbafantasy.databinding.FragmentEditarBinding
import es.unex.nbafantasy.databinding.FragmentListaJugadoresBinding

class ListaJugadoresFragment : Fragment() {

    private var _binding: FragmentListaJugadoresBinding? = null
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentListaJugadoresBinding.inflate(layoutInflater,container, false)
        return binding.root
   }

}