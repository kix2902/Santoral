package es.kix2902.santoral.data

import android.content.Context
import androidx.preference.PreferenceManager
import es.kix2902.santoral.R
import es.kix2902.santoral.helpers.SingletonHolder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PreferenceRepository private constructor(val context: Context) {

    companion object : SingletonHolder<PreferenceRepository, Context>(::PreferenceRepository)

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun showSwipeDateTrace(onResult: (Boolean) -> Unit) {
        doAsync {
            val data = prefs.getBoolean("swipe-date-trace", true)
            uiThread { onResult(data) }
        }
    }

    fun getThemeMode(onResult: (String) -> Unit) {
        doAsync {
            val data =
                prefs.getString("theme-mode", context.getString(R.string.theme_mode_default))!!
            uiThread { onResult(data) }
        }
    }
}