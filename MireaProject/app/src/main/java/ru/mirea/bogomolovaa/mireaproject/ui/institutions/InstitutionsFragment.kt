package ru.mirea.bogomolovaa.mireaproject.ui.institutions

import android.Manifest
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentInstitutionsBinding
import ru.mirea.bogomolovaa.mireaproject.utils.PermissionUtils

class InstitutionsFragment : Fragment() {

    companion object {
        fun newInstance() = InstitutionsFragment()

        @RequiresApi(Build.VERSION_CODES.R)
        private var requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//        Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )

        private val INSTITUTIONS = arrayOf(
            Institutions(
                title = "(В-78)",
                address = "119454, ЦФО, г. Москва, Проспект Вернадского, д. 78",
                location = GeoPoint(55.669956, 37.480225),
            ),
            Institutions(
                title = "(В-86)",
                address = "119571, ЦФО, г. Москва, Проспект Вернадского, д. 86",
                location = GeoPoint(55.661445, 37.477049),
            ),
            Institutions(
                title = "(С-20)",
                address = "107996, ЦФО, г. Москва, ул. Стромынка, д.20",
                location = GeoPoint(55.794229, 37.700772),
            ),
            Institutions(
                title = "(МП-1)",
                address = "119435, ЦФО, г. Москва, улица Малая Пироговская, д. 1, стр. 5",
                location = GeoPoint(55.731582, 37.574840),
            ),
            Institutions(
                title = "(СГ-22)",
                address = "105275, ЦФО, г. Москва, 5-я улица Соколиной Горы, д. 22",
                location = GeoPoint(55.764911, 37.741962),
            ),
            Institutions(
                title = "(Щ-23)",
                address = "115093, ЦФО, г.Москва, 1-й Щипковский переулок, д. 23",
                location = GeoPoint(55.724839, 37.631927),
            ),
            Institutions(
                title = "(У-7)",
                address = "119048, ЦФО, г. Москва, ул. Усачева, д.7/1",
                location = GeoPoint(55.728647, 37.573097),
            ),
            Institutions(
                title = "(К-8)",
                address = "355035, Ставропольский край, г. Ставрополь, Проспект Кулакова, д. 8 в квартале 601",
                location = GeoPoint(45.050309, 41.911205),
            ),
            Institutions(
                title = "(В-2)",
                address = "г. Фрязино, ул. Вокзальная, д.2а (территория предприятия ОАО «НПП «Исток» им. Шокина»)",
                location = GeoPoint(55.964300, 38.046877),
            ),
        )
    }

    data class Institutions(
        val title: String,
        val address: String,
        val location: GeoPoint,
    )

    @RequiresApi(Build.VERSION_CODES.R)
    private val requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            PermissionUtils.handlePermissions(binding.root, permissions, R.string.location_permissions) {
                onPermissionsGranted()
            }
        }

    private val viewModel: InstitutionsViewModel by viewModels()

    private lateinit var _binding: FragmentInstitutionsBinding
    private val binding get() = _binding

    private lateinit var mapView: MapView
    private lateinit var locationNewOverlay: MyLocationNewOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstitutionsBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapView.setZoomRounding(true)
        mapView.setMultiTouchControls(true)

        val mapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(55.794229, 37.700772)
        mapController.setCenter(startPoint)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStart() {
        super.onStart()

        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        mapView.onResume()
        requestPermissionsLauncher.launch(requiredPermissions)
    }

    override fun onStop() {
        super.onStop()

        Configuration.getInstance().save(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        mapView.onPause()
    }


    private fun onPermissionsGranted() {
        locationNewOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        locationNewOverlay.enableMyLocation()
        mapView.overlays.add(this.locationNewOverlay)

        val compassOverlay = CompassOverlay(
            requireContext(), InternalCompassOrientationProvider(
                requireContext()
            ), mapView
        )
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)

        val dm = requireContext().resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView.overlays.add(scaleBarOverlay)

        INSTITUTIONS.forEach {
            val marker = Marker(mapView)
            marker.setPosition(it.location)
            marker.setOnMarkerClickListener { _, _ ->
                Toast.makeText(
                    requireContext(),
                    it.address,
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            mapView.overlays.add(marker)
            marker.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_institution,
                null
            )
            marker.title = it.title
        }
    }
}