package es.kix2902.santoral

import android.content.res.Resources
import java.text.DateFormatSymbols
import java.util.*

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pad: String get() = this.toString().padStart(2, '0')

fun String.toDisplayText(): String {
    val date = this.split("-")
    val month = DateFormatSymbols().months[date[0].toInt() - 1]
    return "${date[1].toInt()} de $month"
}
