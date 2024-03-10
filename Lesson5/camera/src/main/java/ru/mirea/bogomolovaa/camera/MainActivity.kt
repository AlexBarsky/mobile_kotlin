package ru.mirea.bogomolovaa.camera

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.bogomolovaa.camera.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null
    private var isWork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cameraPermissionStatus = ContextCompat.checkSelfPermission(this, CAMERA)
        val storagePermissionStatus = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)

        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED
            && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true
        } else {
            requestPermissions(arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
        }

        val cameraActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
                { result ->
                    if (result.resultCode == RESULT_OK) {
                        binding.imageView.setImageURI(imageUri)
                    }
                }

        binding.imageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Log.d(TAG, isWork.toString())
            if (isWork) {
                try {
                    val photoFile = createImageFile()

                    val authorities = applicationContext.packageName + ".fileprovider"
                    imageUri = FileProvider.getUriForFile(this, authorities, photoFile)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                    cameraActivityResultLauncher.launch(cameraIntent)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                isWork = grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_"

        return File.createTempFile(
            imageFileName, /* file name */
            ".jpg", /* postfix */
            getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
        )
    }

    private companion object {
        const val TAG = "MainActivity"
        const val REQUEST_CODE_PERMISSION = 100
        const val CAMERA_REQUEST = 0
    }
}