package es.kix2902.santoral

import android.content.res.Resources
import android.widget.ImageView
import com.squareup.picasso.Picasso
import es.kix2902.santoral.helpers.CircleTransform
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

fun Int.asOrdinal(): String {
    if (Locale.getDefault().language.equals("es", true)) {
        return "$this"
    }
    return "$this" + if (this % 100 in 11..13) "th" else when (this % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

fun ImageView.loadIconUrl(url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .centerCrop()
        .error(R.mipmap.ic_launcher)
        .transform(CircleTransform())
        .into(this)
}