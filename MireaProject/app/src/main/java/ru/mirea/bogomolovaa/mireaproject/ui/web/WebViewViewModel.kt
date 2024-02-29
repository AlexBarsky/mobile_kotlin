package ru.mirea.bogomolovaa.mireaproject.ui.web

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewViewModel : ViewModel() {

    private val _url = MutableLiveData<String>().apply {
        value = "https://www.example.com/"
    }
    val webView: LiveData<String> = _url
}