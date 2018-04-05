package ninja.irvyne.iwma4earthquakes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_list.*
import ninja.irvyne.iwma4earthquakes.R.attr.layoutManager
import ninja.irvyne.iwma4earthquakes.api.EarthquakeService
import ninja.irvyne.iwma4earthquakes.api.model.EarthquakeData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {

    private lateinit var mService: EarthquakeService
    private lateinit var mExtraTime: String
    private lateinit var mExtraMagnitude: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val mExtraMagnitude = intent.getStringExtra("magnitude") ?: "all"
        val mExtraTime = intent.getStringExtra("time") ?: "day"

        mService = Retrofit.Builder().apply {
            baseUrl("https://earthquake.usgs.gov/")
            addConverterFactory(GsonConverterFactory.create())
            client(OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.HEADERS
                    })
                    .build())

        }.build().create(EarthquakeService::class.java)

        mService.listEarthquakes(magnitude = mExtraMagnitude, time = mExtraTime).enqueue(object : Callback<EarthquakeData> {
            override fun onFailure(call: Call<EarthquakeData>?, t: Throwable?) {
                Log.e(MapsActivity.TAG, "response failed!")
                if (t != null) throw t
                // t?.let { throw it }
            }

            override fun onResponse(call: Call<EarthquakeData>?, response: Response<EarthquakeData>?) {

                Log.d("MapsActivity", response?.body()?.toString())
                listRecyclerView.adapter = EarthquakeAdapter(response!!.features!! as ArrayList)
            }
        })

        listRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListActivity)
            adapter = EarthquakeAdapter()
        }
    }

    companion object {
        private const val TAG = "ListActivity"

        const val EXTRA_MAGNITUDE = "magnitude"
        const val EXTRA_TIME = "time"
    }

}
