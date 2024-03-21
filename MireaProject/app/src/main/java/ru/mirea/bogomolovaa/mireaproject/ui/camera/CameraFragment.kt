package ru.mirea.bogomolovaa.mireaproject.ui.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentCameraBinding
import ru.mirea.bogomolovaa.mireaproject.utils.PermissionUtils
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

    private lateinit var cameraActivityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            PermissionUtils.handlePermissions(binding.root, permissions, R.string.camera_permissions) {
                onPermissionsGranted()
            }
        }

        cameraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                binding.avatar.setImageURI(imageUri)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        requestPermissionsLauncher.launch(arrayOf(
            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ))
    }

    private fun onPermissionsGranted() {
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