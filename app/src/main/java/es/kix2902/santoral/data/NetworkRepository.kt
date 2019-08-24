package es.kix2902.santoral.data

import es.kix2902.santoral.R
import es.kix2902.santoral.data.threads.DefaultExecutorSupplier
import es.kix2902.santoral.pad

object NetworkRepository {

    private val executor: DefaultExecutorSupplier = DefaultExecutorSupplier

    private val santopediaApi by lazy { SantopediaApi.create() }

    fun getDay(
        month: Int,
        day: Int,
        onResult: (List<Model.Saint>) -> Unit,
        onError: (Int) -> Unit
    ) {
        executor.forBackgroundTasks().execute {
            try {
                val response = santopediaApi.getDate("${month.pad}-${day.pad}").execute()

                if (response.isSuccessful) {
                    val list = response.body()!!
                        .toMutableList()
                        .sortedBy { it.fullname }
                        .sortedByDescending { it.important }

                    executor.forMainThreadTasks().execute {
                        onResult(list)
                    }

                } else {
                    executor.forMainThreadTasks().execute {
                        onError(R.string.error_api)
                    }
                }

            } catch (exception: Exception) {
                executor.forMainThreadTasks().execute {
                    onError(R.string.error_api)
                }
            }
        }
    }

    fun getName(name: String, onResult: (List<Model.Saint>) -> Unit, onError: (Int) -> Unit) {
        executor.forBackgroundTasks().execute {
            try {
                val response = santopediaApi.getName(name).execute()

                if (response.isSuccessful) {
                    val list = response.body()!!
                        .toMutableList()
                        .sortedBy { it.fullname }
                        .sortedByDescending { it.important }

                    executor.forMainThreadTasks().execute {
                        onResult(list)
                    }

                } else {
                    executor.forMainThreadTasks().execute {
                        onError(R.string.error_api)
                    }
                }

            } catch (ex: IllegalStateException) {
                executor.forMainThreadTasks().execute {
                    onResult(listOf())
                }

            } catch (exception: Exception) {
                executor.forMainThreadTasks().execute {
                    onError(R.string.error_api)
                }
            }
        }
    }
}