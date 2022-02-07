package codes.adityaa.temasyschat.util

import java.text.SimpleDateFormat
import java.util.*

object Constants {


    fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("ddMMyyyyhhmmss", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        return formatter.format(calendar.time)
    }
}
