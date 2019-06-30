package es.kix2902.santoral.data

import android.content.Context
import es.kix2902.santoral.helpers.SingletonHolder
import java.util.*
import java.util.concurrent.TimeUnit

class DataRepository private constructor(context: Context) {

    companion object : SingletonHolder<DataRepository, Context>(::DataRepository)

    private val networkRepository = NetworkRepository
    private val databaseRepository = DatabaseRepository.getInstance(context)
    private val preferenceRepository = PreferenceRepository.getInstance(context)

    fun getDay(calendar: Calendar, onResult: (List<Model.Saint>) -> Unit, onError: (Int) -> Unit) {
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DATE)

        databaseRepository.getDateSavedForDay(month, day) { dateSaved ->
            if ((dateSaved != null) && ((dateSaved.time - System.currentTimeMillis()) < TimeUnit.DAYS.toMillis(15))) {
                databaseRepository.getSaintsForDay(month, day, onResult = onResult)

            } else {
                networkRepository.getDay(month, day, onResult = { list ->
                    databaseRepository.insertSaints(list)
                    databaseRepository.insertDateSaved(month, day)
                    onResult(list)
                }, onError = onError)
            }
        }
    }

    fun getName(name: String, onResult: (List<Model.Saint>) -> Unit, onError: (Int) -> Unit) {
        networkRepository.getName(name, onResult, onError)
    }

    fun showSwipeDateTrace(onResult: (Boolean) -> Unit) {
        preferenceRepository.showSwipeDateTrace(onResult)
    }

    fun getThemeMode(onResult: (String) -> Unit) {
        preferenceRepository.getThemeMode(onResult)
    }
}