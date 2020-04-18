package com.greenme.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.util.*

fun getStringTimestampFromString(timestamp:String): String {
//    formatOutShort.timeZone = SimpleTimeZone(0, "UTC")
    val date = Date(timestamp.toLong())
    return formatOutShort.format(date)
}

fun getStringTimestampFromLong(timestamp:Long): String {
//    formatOutShort.timeZone = SimpleTimeZone(0, "UTC")
    val date = Date(timestamp)
    return formatOutShort.format(date)
}

fun base64ToBitmap(base64: String): Bitmap {
    val imgbytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imgbytes, 0, imgbytes.size)
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream)
    //        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
}

