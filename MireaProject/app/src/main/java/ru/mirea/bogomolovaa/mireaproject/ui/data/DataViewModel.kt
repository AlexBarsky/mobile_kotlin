package ru.mirea.bogomolovaa.mireaproject.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {

    private val _textTitle = MutableLiveData<String>().apply {
        value = "Книжная индустрия"
    }

    private val _textDescription = MutableLiveData<String>().apply {
        value = "Книжная индустрия включает в себя производство, издание, распространение и продажу книг.\n" +
                "Она включает в себя издательства, книжные магазины, библиотеки, дистрибьюторов и других участников."
    }

    val textViewTitle: LiveData<String> = _textTitle
    val textViewDescription: LiveData<String> = _textDescription
}