package es.unex.nbafantasy

import android.os.Bundle

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat

import es.unex.nbafantasy.bd.roomBD.BD

class AjustesFragment : PreferenceFragmentCompat(){

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferencias, rootKey)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        sharedPreferences?.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
    }

    private val sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == "modoOscuro") {
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

