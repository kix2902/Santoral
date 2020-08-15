package es.kix2902.santoral.data

import es.kix2902.santoral.R
import es.kix2902.santoral.pad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object NetworkRepository {

    private val santopediaApi by lazy { SantopediaApi.create() }

    fun getDay(
        month: Int,
        day: Int,
        onResult: (List<Model.Saint>) -> Unit,
        onError: (Int) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = santopediaApi.getDate("${month.pad}-${day.pad}").execute()

                if (response.isSuccessful) {
                    val list = response.body()!!
                        .toMutableList()
                        .sortedBy { it.name }
                        .sortedByDescending { it.important }

                    withContext(Dispatchers.Main) { onResult(list) }

                } else {
                    withContext(Dispatchers.Main) { onError(R.string.error_api) }
                }

            } catch (exception: Exception) {
                withContext(Dispatchers.Main) { onError(R.string.error_api) }
            }
        }
    }

    fun getName(name: String, onResult: (List<Model.Saint>) -> Unit, onError: (Int) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = santopediaApi.getName(name).execute()

                if (response.isSuccessful) {
                    val list = response.body()!!
                        .toMutableList()
                        .sortedBy { it.date }
                        .sortedByDescending { it.important }

                    withContext(Dispatchers.Main) { onResult(list) }

                } else {
                    withContext(Dispatchers.Main) { onError(R.string.error_api) }
                }

            } catch (ex: IllegalStateException) {
                withContext(Dispatchers.Main) { onResult(listOf()) }

            } catch (exception: Exception) {
                withContext(Dispatchers.Main) { onError(R.string.error_api) }
            }
        }
    }
}