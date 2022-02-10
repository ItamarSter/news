package itamar.stern.news.utils

import android.content.res.Resources
import android.util.TypedValue

fun Number.dp(): Float{
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics)
}

fun createDateString(dateString: String): String {
    val year = dateString.substring(2, 4)
    var month = dateString.substring(5, 7)
    if (month[0] == '0') {
        month = month[1].toString()
    }
    var day = dateString.substring(8, 10)
    if (day[0] == '0') {
        day = day[1].toString()
    }
    var hour = dateString.substring(11, 13)
    if (hour[0] == '0') {
        hour = hour[1].toString()
    }
    val minutes = dateString.substring(14, 16)
    return "$hour:$minutes  $day/$month/$year"
}