package es.unex.nbafantasy.ajustes

import android.os.Bundle

import android.content.SharedPreferences
import android.preference.PreferenceManager

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.preference.PreferenceFragmentCompat
import es.unex.nbafantasy.R

class AjustesFragment : PreferenceFragmentCompat(){
    private  val PREF_MODO_OSCURO = "modoOscuro"
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferencias, rootKey)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        sharedPreferences?.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
    }

    private val sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == PREF_MODO_OSCURO) {
            funcionamientoModoOscuro()
        }
    }

    private fun funcionamientoModoOscuro(){
        val modoOscuroSwitch = PreferenceManager.getDefaultSharedPreferences(requireContext()).all
        val modoOscuro= modoOscuroSwitch["modoOscuro"] as Boolean? ?: false

        if(modoOscuro){
            //seleccionado
            encenderModoOscuro()
        }else{
            //no seleccionado
            apagarModoOscuro()
        }

    }

    private fun encenderModoOscuro(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

    }

    private fun apagarModoOscuro(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener)
    }
}


