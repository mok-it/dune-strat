package hu.mokegyesulet.it.dunestrat

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
    override fun openUrl(url: String) {
        kotlinx.browser.window.open(url, "_blank")
    }
}

actual fun getPlatform(): Platform = JsPlatform()
