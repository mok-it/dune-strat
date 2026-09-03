package hu.mokegyesulet.it.dunestrat.util

import android.content.Intent
import androidx.core.net.toUri

actual fun openURL(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    TODO("Kéne egy Context-et szerezni és megnyitni vele az intentet")
}
