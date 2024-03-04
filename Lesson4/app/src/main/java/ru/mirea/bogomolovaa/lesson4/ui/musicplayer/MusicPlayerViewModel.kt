package ru.mirea.bogomolovaa.lesson4.ui.musicplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicPlayerViewModel : ViewModel() {

    private val _songTitle = MutableLiveData<String>().apply {
        value = "Welcome to the jungle"
    }
    val songTitle: LiveData<String> = _songTitle
}