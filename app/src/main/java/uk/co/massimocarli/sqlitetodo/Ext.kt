package uk.co.massimocarli.sqlitetodo

import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts the content of an InputStream as String. It closes the InputStream in the end
 */
fun InputStream.asString(charset: Charset = Charsets.UTF_8): String {
  this.use {
    val baos = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var bytesRead = it.read(buffer)
    while (bytesRead > 0) {
      baos.write(buffer, 0, bytesRead)
      bytesRead = it.read(buffer)
    }
    val result = String(baos.toByteArray(), charset)
    baos.close()
    return result
  }
}

val DATE_FORMATTER = SimpleDateFormat("dd/MM/yyyy", Locale.UK)

fun String.asDate() = DATE_FORMATTER.parse(this)

fun Date.asString(): String {
  if (this != null) {
    return DATE_FORMATTER.format(this)
  } else {
    return ""
  }
}

fun Long.asDate(): Date {
  val calendar = Calendar.getInstance(Locale.UK)
  calendar.timeInMillis = this
  return calendar.time
}

fun Uri.getId() =
  pathSegments[pathSegments.lastIndex].toLong()