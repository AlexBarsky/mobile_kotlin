package ru.mirea.bogomolovaa.osmmaps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.mirea.bogomolovaa.osmmaps.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    private var requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mapView: MapView
    private lateinit var locationNewOverlay: MyLocationNewOverlay

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
        mapView.setZoomRounding(true)
        mapView.setMultiTouchControls(true)

        val mapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(55.794229, 37.700772)
        mapController.setCenter(startPoint)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStart() {
        super.onStart()

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        mapView.onResume()

        if (allPermissionsGranted()) {
            onPermissionsGranted()
        } else {
            requestPermissions()
        }
    }

    override fun onStop() {
        super.onStop()

        Configuration.getInstance().save(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        mapView.onPause()
    }

    private fun onPermissionsGranted() {
        locationNewOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(applicationContext), mapView)
        locationNewOverlay.enableMyLocation()
        mapView.overlays.add(this.locationNewOverlay)

        val compassOverlay = CompassOverlay(
            applicationContext, InternalCompassOrientationProvider(
                applicationContext
            ), mapView
        )
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)

        val dm = applicationContext.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView.overlays.add(scaleBarOverlay)

        val marker1 = Marker(mapView)
        marker1.setPosition(GeoPoint(55.794229, 37.700772))
        marker1.setOnMarkerClickListener { _, _ ->
            Toast.makeText(
                applicationContext, "Я здесь учусь",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        mapView.overlays.add(marker1)
        marker1.icon = ResourcesCompat.getDrawable(
            getResources(),
            org.osmdroid.library.R.drawable.osm_ic_follow_me_on,
            null
        )
        marker1.title = "MIREA"

        val marker2 = Marker(mapView)
        marker2.setPosition(GeoPoint(55.669956, 37.480225))
        marker2.setOnMarkerClickListener { _, _ ->
            Toast.makeText(
                applicationContext, "И здесь я учусь",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        mapView.overlays.add(marker2)
        marker2.icon = ResourcesCompat.getDrawable(
            getResources(),
            org.osmdroid.library.R.drawable.osm_ic_follow_me_on,
            null
        )
        marker2.title = "MIREA"
    }

    @RequiresApi(Build.VERSION_CODES.R)
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
                onPermissionsGranted()
            } else {
                Snackbar.make(
                    binding.root,
                    "Our app needs access to your device's location. " +
                            "Please grant this permission in your device settings.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Go to settings") {
                    val uri = Uri.parse("package:$packageName")

                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            uri
                        )
                    )
                }.show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun allPermissionsGranted(): Boolean =
        requiredPermissions.all {
            ActivityCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermissions() =
        requestPermissionsLauncher.launch(requiredPermissions)
}