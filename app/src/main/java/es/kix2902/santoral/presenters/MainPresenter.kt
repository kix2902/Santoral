package es.kix2902.santoral.presenters

import android.text.format.DateUtils
import es.kix2902.santoral.activities.MainActivity
import es.kix2902.santoral.data.DataRepository
import es.kix2902.santoral.toCalendar
import java.util.*


class MainPresenter(private val view: MainActivity) {

    private val repository = DataRepository.getInstance(view)

    private var calendar = Calendar.getInstance()

    private var lastFeast: String? = null

    fun loadSwipeDateTracePreference() {
        repository.showSwipeDateTrace { value ->
            view.changeSwipeTraceVisibility(value)
        }
    }

    fun loadSaints() {
        view.showLoading()
        view.clearList()

        val date = DateUtils.formatDateTime(view, calendar.timeInMillis, DateUtils.FORMAT_NO_YEAR)
        view.showDate(date, DateUtils.isToday(calendar.timeInMillis))

        repository.getDay(calendar, onResult = { saints ->
            view.showSaints(saints)
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

    fun getCalendar(): Calendar {
        return calendar
    }

    fun setDate(year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
        loadSaints()
    }

    fun setDateFeast() {
        calendar = lastFeast?.toCalendar()
        loadSaints()
    }

    fun searchName(name: String) {
        view.showLoading()
        repository.getName(name, onResult = { saints ->
            if (saints.isNotEmpty()) {
                val saint = saints.first()
                lastFeast = saint.feast

                val calendarFeast = lastFeast!!.toCalendar()

                view.showNameFeastResult(
                    name,
                    calendarFeast.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("es", "ES")),
                    calendarFeast.get(Calendar.DATE).toString()
                )

            } else {
                lastFeast = null
                view.showNameFeastNoResult(name)
            }
            view.hideLoading()

        }, onError = { cause ->
            lastFeast = null
            view.showMessage(cause)
            view.hideLoading()
        })
    }

    private fun moveDay(quantity: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, quantity)
        loadSaints()
    }
}