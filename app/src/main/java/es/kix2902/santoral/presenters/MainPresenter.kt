package es.kix2902.santoral.presenters

import es.kix2902.santoral.activities.MainActivity
import es.kix2902.santoral.data.DataRepository
import java.util.*

class MainPresenter(private val view: MainActivity) {

    private val repository: DataRepository = DataRepository

    private val calendar = Calendar.getInstance()

    fun loadDay() {
        repository.getDay(calendar, onResult = { saints ->
            val sortedSaints = saints.toMutableList().sortedBy { it.name }.sortedByDescending { it.important }
            view.showSaints(sortedSaints)
        }, onError = { cause ->
            view.showMessage(cause)
        })
    }

}