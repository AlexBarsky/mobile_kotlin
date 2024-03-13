package ru.mirea.bogomolovaa.mireaproject.ui.mail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MailViewModel : ViewModel() {

    val onFileSaved = MutableLiveData<Boolean>()
    val emailTo = MutableLiveData<String>()
    val emailMessage = MutableLiveData<String>()
}