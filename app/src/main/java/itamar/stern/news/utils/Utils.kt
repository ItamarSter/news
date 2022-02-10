package itamar.stern.news.utils

import android.content.res.Resources
import android.util.TypedValue

fun Number.dp(): Float{
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics)
}