package uk.co.agfsoft.skyweather.utils

import android.content.Context
import android.support.v7.app.AlertDialog

fun showAlert(context: Context, titleId: Int, messageId: Int) {
    AlertDialog.Builder(context)
            .setTitle(titleId)
            .setMessage(messageId)
            .create()
            .show()
}