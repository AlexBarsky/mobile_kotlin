package ru.mirea.bogomolovaa.mireaproject.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar


@RequiresApi(Build.VERSION_CODES.R)
class PermissionUtils {
    companion object {
        fun handlePermissions(layout: ViewGroup, permissions:  Map<String, @JvmSuppressWildcards Boolean>, message: Int, callback: () -> Unit) {
            val context = layout.context
            val isGranted = permissions.entries.all {
                it.value
            }
            permissions.entries.forEach {
                Log.e("LOG_TAG", "${it.key} = ${it.value}")
            }

            if (isGranted) {
                callback.invoke()
            } else {
                Snackbar.make(
                    layout.rootView,
                    context.getString(message),
                    Snackbar.LENGTH_LONG
                ).setAction("Go to settings") {
                    val uri = Uri.parse("package:${context.packageName}")

                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            uri
                        )
                    )
                }.show()
            }
        }
    }
}