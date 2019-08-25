package es.kix2902.santoral.data

import es.kix2902.santoral.R
import es.kix2902.santoral.pad
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

object NetworkRepository {

    private val santopediaApi by lazy { SantopediaApi.create() }

    fun getDay(
        month: Int,
        day: Int,
        onResult: (List<Model.Saint>) -> Unit,
        onError: (Int) -> Unit
    ) {
        doAsync {
            try {
                val response = santopediaApi.getDate("${month.pad}-${day.pad}").execute()

                if (response.isSuccessful) {
                    val list = response.body()!!
                        .toMutableList()
                        .sortedBy { it.name }
                        .sortedByDescending { it.important }

                    uiThread { onResult(list) }

                } else {
                    uiThread { onError(R.string.error_api) }
                }

            } catch (exception: Exception) {
                uiThread { onError(R.string.error_api) }
            }
        }
    }

    fun getName(name: String, onResult: (List<Model.Saint>) -> Unit, onError: (Int) -> Unit) {
        doAsync {
            try {
                val response = santopediaApi.getName(name).execute()

                if (response.isSuccessful) {
                    val list = response.body()!!
                        .toMutableList()
                        .sortedBy { it.name }
                        .sortedByDescending { it.important }

                    uiThread { onResult(list) }

                } else {
                    uiThread { onError(R.string.error_api) }
                }

            } catch (ex: IllegalStateException) {
                uiThread { onResult(listOf()) }

            } catch (exception: Exception) {
                uiThread { onError(R.string.error_api) }
            }
        }
    }
}