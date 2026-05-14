package hu.mokegyesulet.it.dunestrat

import java.awt.Desktop
import java.net.URI

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override fun openUrl(url: String) {
        val uri = URI(url)
        try {
            if (Desktop.isDesktopSupported() &&
                Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
            ) {
                Desktop.getDesktop().browse(uri)
            } else {
                fallbackBrowse(url)
            }
        } catch (e: Exception) {
            fallbackBrowse(url)
        }
    }

    private fun fallbackBrowse(url: String) {
        val os = System.getProperty("os.name").lowercase()
        if (os.contains("linux")) {
            val browsers = listOf("xdg-open", "sensible-browser", "google-chrome", "firefox")
            for (browser in browsers) {
                try {
                    Runtime.getRuntime().exec(arrayOf(browser, url))
                    return
                } catch (e: Exception) {
                    // try next
                }
            }
        }
        println("Please open this URL in your browser: $url")
    }
}

actual fun getPlatform(): Platform = JVMPlatform()
