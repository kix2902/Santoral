package es.kix2902.santoral.data

import Model
import es.kix2902.santoral.R
import es.kix2902.santoral.data.threads.DefaultExecutorSupplier
import java.util.*

object NetworkRepository {

    private val executor: DefaultExecutorSupplier = DefaultExecutorSupplier

    private val santopediaApi by lazy { SantopediaApi.create() }

    fun getDay(calendar: Calendar, onResult: (List<Model.ApiResponse>) -> Unit, onError: (Int) -> Unit) {
        executor.forBackgroundTasks().execute {
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DATE)

            val locale: String
            if (Locale.getDefault().language.equals("es", true)) {
                locale = "es_ES"
            } else {
                locale = "en_US"
            }

            val response = santopediaApi.getDay(month, date, locale).execute()

            executor.forMainThreadTasks().execute {
                if (response.isSuccessful) {
                    onResult(response.body()!!)
                } else {
                    onError(R.string.error_api)
                }
            }
        }
    }

    fun getName(name: String, onResult: (List<Model.ApiResponse>) -> Unit, onError: (Int) -> Unit) {
        executor.forBackgroundTasks().execute {
            val locale: String
            if (Locale.getDefault().language.equals("es", true)) {
                locale = "es_ES"
            } else {
                locale = "en_US"
            }

            val response = santopediaApi.getName(name, locale).execute()

            executor.forMainThreadTasks().execute {
                if (response.isSuccessful) {
                    onResult(response.body()!!)
                } else {
                    onError(R.string.error_api)
                }
            }
        }
    }
}