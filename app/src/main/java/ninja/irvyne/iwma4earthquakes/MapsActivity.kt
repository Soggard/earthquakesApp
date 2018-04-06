package ninja.irvyne.iwma4earthquakes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ninja.irvyne.iwma4earthquakes.api.EarthquakeService
import ninja.irvyne.iwma4earthquakes.api.model.EarthquakeData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mService: EarthquakeService
    private lateinit var mExtraMagnitude: String
    private lateinit var mExtraTime: String
    private var mExtraFeatureId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mExtraMagnitude = intent.getStringExtra(EXTRA_MAGNITUDE) ?: "all"
        mExtraTime = intent.getStringExtra(EXTRA_TIME) ?: "day"
        mExtraFeatureId = intent.getStringExtra(EXTRA_FEATURE_ID)

        mService = Retrofit.Builder().apply {
            baseUrl("https://earthquake.usgs.gov/")
            addConverterFactory(GsonConverterFactory.create())
            client(
                    OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.HEADERS
                            })
                            .build()
            )
        }.build().create(EarthquakeService::class.java)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 0f))

        mService.listEarthquakes(magnitude = mExtraMagnitude, time = mExtraTime).enqueue(object : Callback<EarthquakeData> {
            override fun onFailure(call: Call<EarthquakeData>?, t: Throwable?) {
                Log.e(TAG, "response failed!")

                if (t != null) throw t //IDEM: t?.let { throw it }
            }

            override fun onResponse(call: Call<EarthquakeData>?, response: Response<EarthquakeData>?) {
                response?.body()?.features?.forEach { feature ->

                    feature.geometry?.coordinates?.let {
                        val latitude = it[1]
                        val longitude = it[0]
                        val color = if (mExtraFeatureId != null && mExtraFeatureId == feature.id) BitmapDescriptorFactory.HUE_BLUE else when (feature.properties?.mag
                                ?: 0.0) {
                            in 0.0..1.5 -> BitmapDescriptorFactory.HUE_GREEN
                            in 1.5..3.0 -> BitmapDescriptorFactory.HUE_YELLOW
                            in 3.0..4.5 -> BitmapDescriptorFactory.HUE_ORANGE
                            in 4.5..6.0 -> BitmapDescriptorFactory.HUE_RED
                            else -> BitmapDescriptorFactory.HUE_RED
                        }

                        val marker = mMap.addMarker(
                                MarkerOptions()
                                        .position(LatLng(latitude, longitude))
                                        .title(feature.properties?.place ?: "No title")
                                        .snippet(feature.properties?.mag.toString())
                                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                        )

                        if (mExtraFeatureId != null && mExtraFeatureId == feature.id) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 10f))
                            marker.showInfoWindow()
                        }
                    }
                }

                Log.d(TAG, response?.body().toString())
            }
        })
    }

    companion object {
        private const val TAG = "MapsActivity"

        const val EXTRA_MAGNITUDE = "magnitude"
        const val EXTRA_TIME = "time"
        const val EXTRA_FEATURE_ID = "feature_id"
    }
}