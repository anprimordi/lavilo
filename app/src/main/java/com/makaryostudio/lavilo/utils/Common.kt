@file:Suppress("DEPRECATION")

package com.makaryostudio.lavilo.utils

import android.content.Context
import com.makaryostudio.lavilo.R
import java.io.File

// kelas object common
object Common {
    //    dapatkan path aplikasi
    fun getAppPath(context: Context): String {
        val dir = File(
            android.os.Environment.getExternalStorageDirectory().toString()
                    + File.separator
                    + context.resources.getString(R.string.app_name)
                    + File.separator
        )
        if (!dir.exists()) dir.mkdir()
        return dir.path + File.separator
    }
}