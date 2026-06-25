package hu.mokegyesulet.it.dunestrat

import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.UrlLauncher

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
    override fun openUrl(url: String) {
        kotlinx.browser.window.open(url, "_blank")
    }
}

actual fun getPlatform(): Platform = JsPlatform()

@OptIn(SupabaseExperimental::class)
actual fun getUrlLauncher(): UrlLauncher? = null
