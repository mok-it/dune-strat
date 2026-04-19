package hu.mokegyesulet.it.dunestrat

import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.UrlLauncher

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override fun openUrl(url: String) {
        openUrlNative(url)
    }
}

private fun openUrlNative(url: String): Unit = js("window.open(url, '_blank')")

actual fun getPlatform(): Platform = WasmPlatform()

@OptIn(SupabaseExperimental::class)
actual fun getUrlLauncher(): UrlLauncher? = null
