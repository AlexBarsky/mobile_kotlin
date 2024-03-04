package ru.mirea.bogomolovaa.lesson4.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Мой номер по списку: 3"
    }
    private val _onClickText = MutableLiveData<String>().apply {
        value = "Some text on click"
    }
    val text: LiveData<String> = _text
    val onClickText: LiveData<String> = _onClickText
}