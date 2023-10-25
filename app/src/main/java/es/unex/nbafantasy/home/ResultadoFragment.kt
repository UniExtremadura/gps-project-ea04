package es.unex.nbafantasy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import es.unex.nbafantasy.R
import es.unex.nbafantasy.databinding.FragmentResultadoBinding

@AndroidEntryPoint
class ResultadoFragment : Fragment() {
    private var _binding:FragmentResultadoBinding?=null
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentResultadoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}