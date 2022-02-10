package itamar.stern.news.utils

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.annotation.RequiresApi
import itamar.stern.news.NewsApplication
import itamar.stern.news.models.MyError
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun Number.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun createDateString(dateString: String): String {
    //DateString given format: 2022-02-10T17:02:59+00:00
    var localDate = OffsetDateTime.parse(dateString).atZoneSameInstant(ZoneId.of("GMT+2"))
    val year = localDate.year.toString()
    var month = localDate.monthValue.toString()
    if (month[0] == '0') {
        month = month[1].toString()
    }
    var day = localDate.dayOfMonth.toString()
    if (day[0] == '0') {
        day = day[1].toString()
    }
    var hour = localDate.hour.toString()
    if (hour[0] == '0' && hour.length == 2) {
        hour = hour[1].toString()
    }
    var minutes = localDate.minute.toString()
    if(minutes.length == 1) minutes = "0$minutes"
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