package ru.mirea.bogomolovaa.yandexmaps

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    companion object {
        private const val MAPKIT_API_KEY = "21f1d8b1-0001-4aa4-9378-c4b77fcafca3"
    }

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}