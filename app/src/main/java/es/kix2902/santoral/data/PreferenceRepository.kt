package es.kix2902.santoral.data

import android.content.Context
import androidx.preference.PreferenceManager
import es.kix2902.santoral.R
import es.kix2902.santoral.helpers.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreferenceRepository private constructor(val context: Context) {

    companion object : SingletonHolder<PreferenceRepository, Context>(::PreferenceRepository)

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun showSwipeDateTrace(onResult: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val data = prefs.getBoolean("swipe-date-trace", true)
            withContext(Dispatchers.Main) { onResult(data) }
        }
    }

    fun getThemeMode(onResult: (String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val data =
                prefs.getString("theme-mode", context.getString(R.string.theme_mode_default))!!
            withContext(Dispatchers.Main) { onResult(data) }
        }
    }
}