package es.kix2902.santoral.data

import es.kix2902.santoral.R
import es.kix2902.santoral.data.threads.DefaultExecutorSupplier
import es.kix2902.santoral.pad
import java.util.*

object NetworkRepository {

    private val executor: DefaultExecutorSupplier = DefaultExecutorSupplier

    private val santopediaApi by lazy { SantopediaApi.create() }

    fun getDay(month: Int, day: Int, onResult: (List<Model.Saint>) -> Unit, onError: (Int) -> Unit) {
        executor.forBackgroundTasks().execute {
            val locale = if (Locale.getDefault().language.equals("es", true)) {
                "es_ES"
            } else {
                "en_US"
            }

            val response = santopediaApi.getDay(month.pad, day.pad, locale).execute()

            if (response.isSuccessful) {
                val list = response.body()!!
                    .toMutableList()
                    .sortedBy { it.name }
                    .sortedByDescending { it.important }


                list.forEach { saint ->
                    saint.feast = "${month.pad}-${day.pad}"
                    if (locale.equals("en_US")) {
                        saint.url = fixUrl(saint.url)
                    }
                }

                executor.forMainThreadTasks().execute {
                    onResult(list)
                }

            } else {
                executor.forMainThreadTasks().execute {
                    onError(R.string.error_api)
                }
            }
        }
    }

    fun getName(name: String, onResult: (List<Model.Saint>) -> Unit, onError: (Int) -> Unit) {
        executor.forBackgroundTasks().execute {
            val locale = if (Locale.getDefault().language.equals("es", true)) {
                "es_ES"
            } else {
                "en_US"
            }

            val response = santopediaApi.getName(name, locale).execute()

            if (response.isSuccessful) {
                val list = response.body()!!
                    .toMutableList()
                    .sortedBy { it.name }
                    .sortedByDescending { it.important }

                list.forEach { saint ->
                    if (locale.equals("en_US")) {
                        saint.url = fixUrl(saint.url)
                    }
                }

                executor.forMainThreadTasks().execute {
                    onResult(list)
                }

            } else {
                executor.forMainThreadTasks().execute {
                    onError(R.string.error_api)
                }
            }
        }
    }

    private fun fixUrl(url: String): String {
        return url.replace("www", "en").replace("/santos/", "/saint/")
    }
}