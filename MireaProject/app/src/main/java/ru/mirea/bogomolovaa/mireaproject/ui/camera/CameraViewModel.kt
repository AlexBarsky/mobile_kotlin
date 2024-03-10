package ru.mirea.bogomolovaa.mireaproject.ui.camera

import android.net.Uri
import android.os.Environment
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CameraViewModel : ViewModel() {
//    private val _images = MutableLiveData<List<Uri>>()
//    val images: LiveData<List<Uri>> = _images
//
//    init {
//        // Загрузка изображений из хранилища
//        viewModelScope.launch {
//            val imagesList = loadImagesFromStorage()
//            _images.value = imagesList
//        }
//    }
//
//    fun addImage(image: Uri) {
//        // Сохранение изображения в хранилище
//        viewModelScope.launch {
//            saveImageToStorage(image)
//            val imagesList = loadImagesFromStorage()
//            _images.value = imagesList
//        }
//    }
//
//    private suspend fun loadImagesFromStorage(): List<Uri> {
//        // Загрузка изображений из хранилища
//        val images = mutableListOf<Uri>()
//        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        if (imageDirectory != null) {
//            imageDirectory.listFiles().forEach { file ->
//                if (file.isFile && file.name.endsWith(".jpg", true)) {
//                    images.add(FileProvider.getUriForFile(application, application.packageName + ".fileprovider", file))
//                }
//            }
//        }
//        return imagesList
//    }
//
//    private suspend fun saveImageToStorage(image: Uri) {
//        val imageFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image.jpg")
//        val inputStream = requireContext().contentResolver.openInputStream(image)
//        val outputStream = FileOutputStream(imageFile)
//        inputStream?.copyTo(outputStream)
//        inputStream?.close()
//        outputStream.flush()
//        outputStream.close()
//    }
//
//    fun hasImages(): Boolean {
//        // Проверка наличия изображений
//        return _images.value?.isNotEmpty() ?: false
//    }
}