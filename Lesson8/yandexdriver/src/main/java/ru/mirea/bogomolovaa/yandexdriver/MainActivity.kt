package ru.mirea.bogomolovaa.yandexdriver

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.location.Location
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CompositeIcon
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.coroutines.launch
import ru.mirea.bogomolovaa.yandexdriver.databinding.ActivityMainBinding
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MainActivity : AppCompatActivity(),
    DrivingSession.DrivingRouteListener,
    UserLocationObjectListener {

    companion object {
        private val FAVORITE_LOCATION: Point = Point(37.4143, -122.0777)
        private val colors = intArrayOf(-0x10000, -0xff0100, 0x00FFBBBB, -0xffff01)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private var locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapView: MapView
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingSession: DrivingSession
    private lateinit var marker: PlacemarkMapObject

    private lateinit var routeStartLocation: Point
    private lateinit var routeEndLocation: Point

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
                permissionsGranted()
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
        mapView.mapWindow.map.isRotateGesturesEnabled = false

        drivingRouter = DirectionsFactory
            .getInstance()
            .createDrivingRouter(DrivingRouterType.ONLINE)
        mapObjects = mapView.mapWindow.map.mapObjects.addCollection()

        marker = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            setIcon(ImageProvider.fromResource(baseContext, R.drawable.marker_icon))
        }

        marker.addTapListener { _, _ ->
            Toast.makeText(
                application, "Marker click",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()

        if (checkPermission()) {
            permissionsGranted()
        } else {
            requestPermissions()
        }
    }

    private fun permissionsGranted() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        loadUserLocationLayer()

        lifecycleScope.launch {
            setRouteLocations()

            mapView.mapWindow.map.move(
                CameraPosition(
                    Point(
                        (routeStartLocation.latitude + routeEndLocation.latitude) / 2,
                        (routeStartLocation.longitude + routeEndLocation.longitude) / 2
                    ),
                    12F,
                    0F,
                    0F
                )
            )
            marker.geometry = routeEndLocation
//            createMarker()
            submitRequest()
        }
    }

    private fun loadUserLocationLayer() {
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)
    }

    private suspend fun setRouteLocations() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            val location = fusedLocationClient.awaitLastLocation()
            location?.let {
                routeStartLocation = Point(it.latitude, it.longitude)
                routeEndLocation = FAVORITE_LOCATION
                Log.d("TAG", "${it.latitude}, ${it.longitude}")
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun FusedLocationProviderClient.awaitLastLocation(): Location? {
        return suspendCoroutine { continuation ->
            lastLocation
                .addOnSuccessListener { location ->
                    continuation.resume(location)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }

    private fun createMarker() {
        val marker = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = routeEndLocation
            setIcon(ImageProvider.fromResource(baseContext, R.drawable.marker_icon))
        }

        marker.addTapListener { _, _ ->
            Toast.makeText(
                application, "Marker click",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    private fun submitRequest() {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        // Кол-во альтернативных путей
        drivingOptions.setRoutesCount(4)
        val requestPoints = ArrayList<RequestPoint>()
        // Устанавка точек маршрута
        requestPoints.add(
            RequestPoint(
                routeStartLocation,
                RequestPointType.WAYPOINT,
                null,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                routeEndLocation,
                RequestPointType.WAYPOINT,
                null,
                null
            )
        )
        // Отправка запроса на сервер
        drivingSession = drivingRouter.requestRoutes(
            requestPoints, drivingOptions,
            vehicleOptions, this
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissions() =
        requestPermissionsLauncher.launch(locationPermissions)

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
        for (i in 0..<p0.size) {
            mapObjects.addPolyline(p0[i].geometry).setStrokeColor(colors[i])
        }
    }

    override fun onDrivingRoutesError(p0: Error) {
        var errorMessage = getString(R.string.unknown_error_message)
        if (p0 is RemoteError) {
            errorMessage = getString(R.string.remote_error_message)
        } else if (p0 is NetworkError) {
            errorMessage = getString(R.string.network_error_message)
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )

        // При определении направления движения устанавливается следующая иконка
        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                baseContext, R.drawable.arrow_up_float
            )
        )

        // При получении координат местоположения устанавливается следующая иконка
        val pinIcon: CompositeIcon = userLocationView.pin.useCompositeIcon()
        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(baseContext, R.drawable.search_result),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
}