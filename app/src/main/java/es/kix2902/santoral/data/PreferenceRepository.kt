package es.kix2902.santoral.data

import android.content.Context
import androidx.preference.PreferenceManager
import es.kix2902.santoral.R
import es.kix2902.santoral.data.threads.DefaultExecutorSupplier
import es.kix2902.santoral.helpers.SingletonHolder

class PreferenceRepository private constructor(val context: Context) {

    companion object : SingletonHolder<PreferenceRepository, Context>(::PreferenceRepository)

    private val executor = DefaultExecutorSupplier

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun showSwipeDateTrace(onResult: (Boolean) -> Unit) {
        executor.forBackgroundTasks().execute {
            val data = prefs.getBoolean("swipe-date-trace", true)
            executor.forMainThreadTasks().execute {
                onResult(data)
            }
        }
    }

    fun getThemeMode(onResult: (String) -> Unit) {
        executor.forBackgroundTasks().execute {
            val data = prefs.getString("theme-mode", context.getString(R.string.theme_mode_default))!!
            executor.forMainThreadTasks().execute {
                onResult(data)
            }
        }
    }
}