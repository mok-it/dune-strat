package hu.mokegyesulet.it.dunestrat

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override fun openUrl(url: String) {
        // Supabase Auth handles this natively or via dedicated launcher on Android
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()
