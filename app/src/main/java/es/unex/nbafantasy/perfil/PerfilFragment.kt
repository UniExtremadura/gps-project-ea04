package es.unex.nbafantasy.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.MainActivity
import es.unex.nbafantasy.databinding.FragmentPerfilBinding
import es.unex.nbafantasy.utils.ComprobacionCredenciales
import kotlinx.coroutines.launch


class PerfilFragment : Fragment() {
    private lateinit var _binding: FragmentPerfilBinding
    private val binding get()=_binding!!
    private val viewModel: PerfilViewModel by viewModels { PerfilViewModel.Factory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarDatosUsuario()
        actualizarDatos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentPerfilBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    private fun cargarDatosUsuario(){
        lifecycleScope.launch {
            viewModel.usuario = (requireActivity() as? MainActivity)?.getUsuario()!!
            val usuario = viewModel.buscarId()
            with(binding) {
                etNombre.setText(usuario.nombre)
                etContrasena1.setText(usuario.contrasena)
                etContrasena2.setText(usuario.contrasena)
            }
        }
    }

    private fun actualizarDatos(){
        with(binding) {
            btGuardar.setOnClickListener {
                lifecycleScope.launch {
                    val comprobar = ComprobacionCredenciales.registro(
                        etNombre.text.toString(),
                        etContrasena1.text.toString(),
                        etContrasena2.text.toString()
                    )

                    if (comprobar.fallo) {
                        notificarMensaje(comprobar.msg)
                    } else if (viewModel.busquedaNombre(binding.etNombre.text.toString()) != null &&
                        viewModel.buscarIdByNombre(binding.etNombre.text.toString())?.toLong() != viewModel.usuario!!.usuarioId) {
                        notificarMensaje("Nombre de usuario ocupado")
                    } else {
                        viewModel.actualizarUsuario(etNombre.text.toString(), etContrasena1.text.toString())
                        notificarMensaje("Usuario actualizado")
                    }
                }
            }
        }
    }

    private fun notificarMensaje(mensaje: String){
        Toast.makeText( getContext(),mensaje,Toast.LENGTH_SHORT).show()
    }
}