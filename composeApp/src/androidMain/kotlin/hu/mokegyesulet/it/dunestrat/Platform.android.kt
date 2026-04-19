package hu.mokegyesulet.it.dunestrat

import android.os.Build
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.UrlLauncher

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override fun openUrl(url: String) {
        // Supabase Auth handles this natively or via dedicated launcher on Android
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()

@OptIn(SupabaseExperimental::class)
actual fun getUrlLauncher(): UrlLauncher? = null
