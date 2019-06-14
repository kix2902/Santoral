package es.kix2902.santoral.data

import Model
import es.kix2902.santoral.R
import es.kix2902.santoral.data.threads.DefaultExecutorSupplier
import es.kix2902.santoral.pad
import java.util.*

object NetworkRepository {

    private val executor: DefaultExecutorSupplier = DefaultExecutorSupplier

    private val santopediaApi by lazy { SantopediaApi.create() }

    fun getDay(calendar: Calendar, onResult: (List<Model.ApiResponse>) -> Unit, onError: (Int) -> Unit) {
        executor.forBackgroundTasks().execute {
            val month = calendar.get(Calendar.MONTH) + 1
            val date = calendar.get(Calendar.DATE)

            val locale = if (Locale.getDefault().language.equals("es", true)) {
                "es_ES"
            } else {
                "en_US"
            }

            val response = santopediaApi.getDay(month.pad, date.pad, locale).execute()

            var list: MutableList<Model.ApiResponse> = mutableListOf()
            if (locale.equals("en_US")) {
                response.body()?.let {
                    list = it.toMutableList()
                    list.forEach { it.url = fixUrl(it.url) }
                }
            }

            executor.forMainThreadTasks().execute {
                if (response.isSuccessful) {
                    onResult(list)
                } else {
                    onError(R.string.error_api)
                }
            }
        }
    }

    fun getName(name: String, onResult: (List<Model.ApiResponse>) -> Unit, onError: (Int) -> Unit) {
        executor.forBackgroundTasks().execute {
            val locale = if (Locale.getDefault().language.equals("es", true)) {
                "es_ES"
            } else {
                "en_US"
            }

            val response = santopediaApi.getName(name, locale).execute()

            var list: MutableList<Model.ApiResponse> = mutableListOf()
            if (locale.equals("en_US")) {
                response.body()?.let {
                    list = it.toMutableList()
                    list.forEach { it.url = fixUrl(it.url) }
                }
            }

            executor.forMainThreadTasks().execute {
                if (response.isSuccessful) {
                    onResult(list)
                } else {
                    onError(R.string.error_api)
                }
            }
        }
    }

    private fun fixUrl(url: String): String {
        return url.replace("www", "en").replace("/santos/", "/saint/")
    }
}