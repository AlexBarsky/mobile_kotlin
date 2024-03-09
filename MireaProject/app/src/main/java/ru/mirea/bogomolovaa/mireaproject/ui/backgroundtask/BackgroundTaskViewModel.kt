package ru.mirea.bogomolovaa.mireaproject.ui.backgroundtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mirea.bogomolovaa.mireaproject.R

class BackgroundTaskViewModel : ViewModel() {

    private val _textDescription = MutableLiveData<Int>().apply {
        value = R.string.background_task_description
    }
    val resultTextView = MutableLiveData<String>()
    val textViewDescription: LiveData<Int> = _textDescription
}