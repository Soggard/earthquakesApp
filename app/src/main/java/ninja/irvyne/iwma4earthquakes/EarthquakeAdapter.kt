package ninja.irvyne.iwma4earthquakes

import android.graphics.Color
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import ninja.irvyne.iwma4earthquakes.api.model.Feature

class EarthquakeAdapter(
        private val mListener: EarthquakeAdapterInteractions,
        private val myDataset: ArrayList<Feature>
) : RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val magnitude: TextView = view.findViewById(R.id.earthquakeMagnitude)
        val title: TextView = view.findViewById(R.id.earthquakeTitle)
        val snippet: TextView = view.findViewById(R.id.earthquakeSnippet)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeAdapter.ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_earthquake, parent, false)
    )

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feature = myDataset[position]
        val color = when (feature.properties?.mag ?: 0.0) {
            else -> Color.RED
        }

        // Change color of background
        DrawableCompat.setTint(holder.magnitude.background, color)
        holder.magnitude.text = "%.1f".format(feature.properties?.mag)
        holder.title.text = feature.properties?.place
        holder.snippet.text = feature.properties?.title

        holder.view.setOnClickListener {
            mListener.onEarthquakeSelected(feature)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    interface EarthquakeAdapterInteractions {
        fun onEarthquakeSelected(earthquake: Feature)
    }
}