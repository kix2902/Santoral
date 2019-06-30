package es.kix2902.santoral.presenters

import android.text.format.DateUtils
import es.kix2902.santoral.activities.MainActivity
import es.kix2902.santoral.asOrdinal
import es.kix2902.santoral.data.DataRepository
import es.kix2902.santoral.toCalendar
import java.util.*


class MainPresenter(private val view: MainActivity) {

    private val repository = DataRepository.getInstance(view)

    private var calendar = Calendar.getInstance()

    fun loadSwipeDateTracePreference() {
        repository.showSwipeDateTrace { value ->
            view.changeSwipeTraceVisibility(value)
        }
    }

    fun loadSaints() {
        view.showLoading()

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

    fun searchName(name: String) {
        view.showLoading()
        repository.getName(name, onResult = { saints ->
            if (saints.isNotEmpty()) {
                val saint = saints.first()

                val feast = saint.feast!!.toCalendar()

                view.showNameFeastResult(
                    String.format(
                        view.getString(es.kix2902.santoral.R.string.feast_for_name),
                        name,
                        feast.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
                        feast.get(Calendar.DATE).asOrdinal()
                    )
                )

            } else {
                view.showNameFeastResult(
                    String.format(view.getString(es.kix2902.santoral.R.string.no_date_for_name), name)
                )
            }
            view.hideLoading()

        }, onError = { cause ->
            view.showMessage(cause)
            view.hideLoading()
        })
    }

    private fun moveDay(quantity: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, quantity)
        loadSaints()
    }
}