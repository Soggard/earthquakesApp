package ninja.irvyne.iwma4earthquakes.extension

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ninja.irvyne.iwma4earthquakes.MapsActivity
import kotlin.reflect.KClass

inline fun <T: AppCompatActivity> KClass<T>.start(activity: AppCompatActivity, finish: Boolean = false) {

    Intent(activity, this.java).apply {
        activity.startActivity(this)
    }
}
