package com.greenme.util

import android.content.Context
import android.content.Intent

private const val PACKAGE_NAME = ""

private fun intentTo(context: Context, className: String): Intent =
    Intent(Intent.ACTION_VIEW).setClassName(context, className)

internal fun String.loadIntentOrNull(context: Context): Intent? =
    try {
        Class.forName(this).run { intentTo(context, this@loadIntentOrNull) }
    } catch (e: ClassNotFoundException) {
        null
    }