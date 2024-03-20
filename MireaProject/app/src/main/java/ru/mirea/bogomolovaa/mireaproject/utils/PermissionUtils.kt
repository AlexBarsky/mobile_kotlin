package ru.mirea.bogomolovaa.mireaproject.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import ru.mirea.bogomolovaa.mireaproject.ui.institutions.InstitutionsFragment

@RequiresApi(Build.VERSION_CODES.R)
class PermissionUtils {

    companion object {
//        fun requestPermissionsLauncher(context: Context): ActivityResultLauncher<Array<String>> =
//            context.registerForActivityResult(
//                ActivityResultContracts.RequestMultiplePermissions()
//            ) { permissions ->
//                val granted = permissions.entries.all {
//                    it.value
//                }
//                permissions.entries.forEach {
//                    Log.e("LOG_TAG", "${it.key} = ${it.value}")
//                }
//
//                if (granted) {
//                    onPermissionsGranted()
//                } else {
//                    Snackbar.make(
//                        binding.root,
//                        "Our app needs access to your device's location. " +
//                                "Please grant this permission in your device settings.",
//                        Snackbar.LENGTH_INDEFINITE
//                    ).setAction("Go to settings") {
//                        val uri = Uri.parse("package:${requireContext().packageName}")
//
//                        startActivity(
//                            Intent(
//                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                uri
//                            )
//                        )
//                    }.show()
//                }
//            }

        fun hasPermissions(context: Context, requiredPermissions: Array<String>): Boolean =
            requiredPermissions.all {
                ActivityCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }

//        fun requestPermissions(activity: Activity, requestCode: Int) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                try {
//                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//                    intent.addCategory("android.intent.category.DEFAULT")
//                    intent.setData(Uri.parse(String.format("package:%s", activity.packageName)))
//                    activity.startActivityForResult(intent, requestCode)
//                } catch (e: Exception) {
//                    val intent = Intent()
//                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//                    activity.startActivityForResult(intent, requestCode)
//                }
//            } else {
//                ActivityCompat.requestPermissions(
//                    activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    requestCode
//                )
//            }
//        }
    }
}