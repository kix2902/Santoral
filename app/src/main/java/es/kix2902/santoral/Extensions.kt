package es.kix2902.santoral

import android.content.res.Resources

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pad: String get() = this.toString().padStart(2, '0')