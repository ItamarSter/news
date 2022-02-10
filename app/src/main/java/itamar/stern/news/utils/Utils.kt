package itamar.stern.news.utils

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import itamar.stern.news.NewsApplication
import itamar.stern.news.models.MyError
import java.time.LocalDateTime

fun Number.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
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

fun sendErrorsToFirebase(e: Exception) {

    NewsApplication.fireDBRef.child("errors").push().setValue(
        MyError(
            e.stackTraceToString(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) LocalDateTime.now().toString() else System.currentTimeMillis().toString(),
            if (NewsApplication.account != null) NewsApplication.account!!.displayName!! else "none"
        )
    )

}