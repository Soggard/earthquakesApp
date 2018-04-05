package ninja.irvyne.iwma4earthquakes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ninja.irvyne.iwma4earthquakes.extension.blabla

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mutableListOf("Hour", "Day", "Week", "Month"))
        val magnitudeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mutableListOf("All", "M1+", "M2.5+", "M4.5+", "Significant"))

        mainMagnitudeSpinner.adapter = magnitudeAdapter
        mainTimeSpinner.adapter = timeAdapter

        mainButton.setOnClickListener {
            val magnitude = when (mainMagnitudeSpinner.selectedItemPosition) {
                0 -> "all"
                1 -> "1.0"
                2 -> "2.5"
                3 -> "4.5"
                4 -> "significant"
                else -> throw Exception("Item unknown")
            }

            val time = when (mainTimeSpinner.selectedItemPosition) {
                0 -> "hour"
                1 -> "day"
                2 -> "week"
                3 -> "month"
                else -> throw Exception("Item unknown")
            }

            startActivity(Intent(this, MapsActivity::class.java).apply {
                putExtra(MapsActivity.EXTRA_MAGNITUDE, magnitude)
                putExtra(MapsActivity.EXTRA_TIME, time)
            })
        }

        mainListButton.setOnClickListener {
            val magnitude = when (mainMagnitudeSpinner.selectedItemPosition) {
                0 -> "all"
                1 -> "1.0"
                2 -> "2.5"
                3 -> "4.5"
                4 -> "significant"
                else -> throw Exception("Item unknown")
            }

            val time = when (mainTimeSpinner.selectedItemPosition) {
                0 -> "hour"
                1 -> "day"
                2 -> "week"
                3 -> "month"
                else -> throw Exception("Item unknown")
            }

            startActivity(Intent(this, ListActivity::class.java).apply {
                putExtra(ListActivity.EXTRA_MAGNITUDE, magnitude)
                putExtra(ListActivity.EXTRA_TIME, time)
            })
        }
    }


}
