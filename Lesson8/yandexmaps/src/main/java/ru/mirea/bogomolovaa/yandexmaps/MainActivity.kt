package ru.mirea.bogomolovaa.yandexmaps

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CompositeIcon
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import ru.mirea.bogomolovaa.yandexmaps.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), UserLocationObjectListener{

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private var locationPermissions = arrayOf(
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION,
        ACCESS_BACKGROUND_LOCATION
    )

    private var requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            permissions.entries.forEach {
                Log.e("LOG_TAG", "${it.key} = ${it.value}")
            }

            if (granted) {
                mapView.onStart()
                MapKitFactory.getInstance().onStart()
                loadUserLocationLayer()
            } else {
                Snackbar.make(
                    binding.root,
                    "Our app needs access to your device's location. " +
                            "Please grant this permission in your device settings.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Go to settings") {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            }
        }

    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapView: MapView

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapView = binding.mapview
//
//        requestPermissionsLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            if (permissions.keys.all {
//                ContextCompat.checkSelfPermission(baseContext, it) != PackageManager.PERMISSION_GRANTED
//            }) {
//                Log.d("TAG", "Permissions not granted")
//                Snackbar.make(
//                    binding.root,
//                    "Our app needs access to your device's location. " +
//                            "Please grant this permission in your device settings.",
//                    Snackbar.LENGTH_LONG
//                ).setAction("Go to settings") {
//                    try {
//                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                        intent.data = Uri.fromParts(
//                            "package",
//                            baseContext.packageName,
//                            null
//                        )
//                        startActivity(intent)
//                    } catch (e: ActivityNotFoundException) {
//                        Snackbar.make(
//                            binding.root,
//                            "Unable to open settings. " +
//                                    "Please open your device settings and grant the permission manually.",
//                            Snackbar.LENGTH_LONG
//                        ).show()
//                    }
//                }.show()
//            } else {
//                Log.d("TAG", "Permissions granted")
//                mapView.onStart()
//                MapKitFactory.getInstance().onStart()
//                loadUserLocationLayer()
//            }
//        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == 1000 && grantResults.isNotEmpty()) {
//            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                // Permissions granted, start MapView and MapKit
//                mapView.onStart()
//                MapKitFactory.getInstance().onStart()
//                loadUserLocationLayer()
//            } else {
//                Log.d("TAG", "Permissions not granted")
//                Snackbar.make(
//                    binding.root,
//                    "Our app needs access to your device's location. " +
//                            "Please grant this permission in your device settings.",
//                    Snackbar.LENGTH_LONG
//                ).setAction("Go to settings") {
//                    try {
//                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                        intent.data = Uri.fromParts(
//                            "package",
//                            baseContext.packageName,
//                            null
//                        )
//                        startActivity(intent)
//                    } catch (e: ActivityNotFoundException) {
//                        Snackbar.make(
//                            binding.root,
//                            "Unable to open settings. " +
//                                    "Please open your device settings and grant the permission manually.",
//                            Snackbar.LENGTH_LONG
//                        ).show()
//                    }
//                }.show()
//            }
//        }
//    }

    private fun loadUserLocationLayer() {
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)
        Log.d("TAG", "UserLocationLayer loaded")
    }

    override fun onStop() {
        super.onStop()
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart()

        if (checkPermission()) {
            mapView.onStart()
            MapKitFactory.getInstance().onStart()
            loadUserLocationLayer()
        } else {
            requestPermissions()
        }
//        requestPermissionsLauncher.launch(locationPermissions)
//        requestPermissions(locationPermissions, 1000)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this, ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissions() =
        requestPermissionsLauncher.launch(locationPermissions)


    override fun onObjectAdded(userLocationView: UserLocationView) {
        Log.d("TAG", "onObjectAdded")
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )
        Log.d("TAG", "setAnchor")

        // При определении направления движения устанавливается следующая иконка
        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                this, R.drawable.arrow_up_float
            )
        )
        Log.d("TAG", "setIcon")

        // При получении координат местоположения устанавливается следующая иконка
        val pinIcon: CompositeIcon = userLocationView.pin.useCompositeIcon()
        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(this, R.drawable.search_result),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )
        Log.d("TAG", "setNextIcon")

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
}