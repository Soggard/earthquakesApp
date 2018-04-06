package ninja.irvyne.iwma4earthquakes.extension

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import kotlin.reflect.KClass

inline fun <T: AppCompatActivity> KClass<T>.start(activity: AppCompatActivity) {
    Intent(activity, this.java).apply {
        activity.startActivity(this)
    }
}