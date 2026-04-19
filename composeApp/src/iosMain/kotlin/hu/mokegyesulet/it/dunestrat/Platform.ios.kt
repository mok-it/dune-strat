package hu.mokegyesulet.it.dunestrat

import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.UrlLauncher
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override fun openUrl(url: String) {
        // Handled natively
    }
}

actual fun getPlatform(): Platform = IOSPlatform()

@OptIn(SupabaseExperimental::class)
actual fun getUrlLauncher(): UrlLauncher? = null
