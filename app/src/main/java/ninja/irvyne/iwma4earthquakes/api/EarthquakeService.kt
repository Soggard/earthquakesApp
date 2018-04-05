package ninja.irvyne.iwma4earthquakes.api

import ninja.irvyne.iwma4earthquakes.api.model.EarthquakeData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EarthquakeService {

    @GET("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/{magnitude}_{time}.geojson")
    fun listEarthquakes(@Path("magnitude") magnitude: String = "all", @Path("time") time: String = "day"): Call<EarthquakeData>
}