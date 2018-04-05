package ninja.irvyne.iwma4earthquakes.extension

import android.content.Context
import android.widget.Toast

inline fun Context.blabla(message: String): Toast {
    return Toast.makeText(this, "rerer", Toast.LENGTH_SHORT).apply {
        this.show()
    }
}