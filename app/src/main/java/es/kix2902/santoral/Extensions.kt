package es.kix2902.santoral

import android.content.res.Resources
import java.util.*

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pad: String get() = this.toString().padStart(2, '0')

fun String.toCalendar(): Calendar {
    val date = this.split("-")
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, date[0].toInt() - 1)
    calendar.set(Calendar.DATE, date[1].toInt())
    return calendar
}
