package uk.co.agfsoft.skyweather.utils

import android.content.Context
import android.util.Log
import javax.inject.Inject

/**
 * Created by afa28 on 06/02/2018.
 */
class ContextLogger @Inject constructor (private val context: Context) {
    fun logContext() {
        Log.d("contextLogger", context.toString())
    }
}