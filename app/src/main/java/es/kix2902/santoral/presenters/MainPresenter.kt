package es.kix2902.santoral.presenters

import android.text.format.DateUtils
import es.kix2902.santoral.activities.MainActivity
import es.kix2902.santoral.data.DataRepository
import java.util.*

class MainPresenter(private val view: MainActivity) {

    private val repository = DataRepository.getInstance(view)

    private var calendar = Calendar.getInstance()

    fun loadSaints() {
        view.showLoading()

        val date = DateUtils.formatDateTime(view, calendar.timeInMillis, DateUtils.FORMAT_NO_YEAR)
        view.showDate(date, DateUtils.isToday(calendar.timeInMillis))

        repository.getDay(calendar, onResult = { saints ->
            val sortedSaints = saints.toMutableList().sortedBy { it.name }.sortedByDescending { it.important }
            view.showSaints(sortedSaints)
            view.hideLoading()

        }, onError = { cause ->
            view.showMessage(cause)
            view.hideLoading()
        })
    }

    fun today() {
        calendar = Calendar.getInstance()
        loadSaints()
    }

    fun nextDay() {
        moveDay(+1)
    }

    fun previousDay() {
        moveDay(-1)
    }

    private fun moveDay(quantity: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, quantity)
        loadSaints()
    }
}