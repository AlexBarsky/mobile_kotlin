package ru.mirea.bogomolovaa.mireaproject.ui.camera

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentCameraBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraFragment : Fragment() {

    companion object {
        fun newInstance() = CameraFragment()
        val TAG: String = CameraFragment::class.java.simpleName
    }

    private val viewModel: CameraViewModel by activityViewModels()
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        it.key
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (shouldShowRequestPermissionRationale(it.key)) {
                        Snackbar.make(
                            binding.root,
                            "Our app needs access to your device's microphone to record audio. " +
                                    "Please grant this permission in your device settings.",
                            Snackbar.LENGTH_LONG
                        ).setAction("Go to settings") {
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.data = Uri.fromParts(
                                    "package",
                                    requireContext().packageName,
                                    null
                                )
                                startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Snackbar.make(
                                    binding.root,
                                    "Unable to open settings. " +
                                            "Please open your device settings and grant the permission manually.",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }.show()
                    }
                }
            }
        }

        val cameraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                binding.avatar.setImageURI(imageUri)
            }
        }

        binding.avatar.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                val photoFile = createImageFile()

                val authorities = requireContext().packageName + ".fileprovider"
                imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                cameraActivityResultLauncher.launch(cameraIntent)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        requestPermissionsLauncher.launch(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ))
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_"

        return File.createTempFile(
            imageFileName, /* file name */
            ".jpg", /* postfix */
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
        )
    }
}