package es.unex.nbafantasy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.unex.nbafantasy.Data.UsuarioRepository
import es.unex.nbafantasy.bd.elemBD.Usuario
import es.unex.nbafantasy.bd.roomBD.BD
import es.unex.nbafantasy.databinding.FragmentPerfilBinding
import es.unex.nbafantasy.utils.ComprobacionCredenciales
import kotlinx.coroutines.launch


class PerfilFragment : Fragment() {
    private lateinit var bd: BD
    private lateinit var usuario: Usuario
    private lateinit var _binding: FragmentPerfilBinding
    private val binding get()=_binding!!
    private lateinit var repositoryUsuario: UsuarioRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializacion BD
        bd= BD.getInstance(requireContext())!!
        repositoryUsuario =UsuarioRepository.getInstance(bd.usuarioDao())

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
            usuario = (requireActivity() as? MainActivity)?.getUsuario()!!
            usuario = buscarId()
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
                    } else if (busquedaNombre(binding.etNombre.text.toString()) != null &&
                        buscarIdByNombre(binding.etNombre.text.toString())?.toLong() !=usuario.usuarioId) {
                        notificarMensaje("Nombre de usuario ocupado")
                    } else {
                        actualizarUsuario(etNombre.text.toString(), etContrasena1.text.toString())
                        notificarMensaje("Usuario actualizado")
                    }
                }
            }
        }
    }

    private suspend fun actualizarUsuario(nuevoNombre:String, nuevaContrasena:String){
        repositoryUsuario.actualizar(usuario.usuarioId?:0,nuevoNombre,nuevaContrasena)
    }

    private suspend fun busquedaNombre(nombreUsuario:String): Usuario{
        return repositoryUsuario.busquedaNombre(nombreUsuario)
    }

    private suspend fun buscarIdByNombre(nombreUsuario:String): Int{
        return repositoryUsuario.buscarIdByNombre(nombreUsuario)
    }

    private suspend fun buscarId():Usuario{
        return repositoryUsuario.buscarId(usuario.usuarioId?:0)
    }

    private fun notificarMensaje(mensaje: String){
        Toast.makeText( getContext(),mensaje,Toast.LENGTH_SHORT).show()
    }
}