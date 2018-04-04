package ninja.irvyne.iwma4earthquakes.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EarthquakeData (

        @SerializedName("type")
    @Expose
    var type: String? = null,

        @SerializedName("metadata")
    @Expose
    var metadata: Metadata? = null,

        @SerializedName("features")
    @Expose
    var features: List<Feature>? = null,

        @SerializedName("bbox")
    @Expose
    var bbox: List<Double>? = null

)
