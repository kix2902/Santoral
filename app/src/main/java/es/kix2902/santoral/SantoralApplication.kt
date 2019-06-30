package es.kix2902.santoral

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import es.kix2902.santoral.data.DataRepository

class SantoralApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val repository = DataRepository.getInstance(this)
        repository.getThemeMode { value ->
            when (value) {
                "never" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                "battery" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
                "system" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                "always" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }
}