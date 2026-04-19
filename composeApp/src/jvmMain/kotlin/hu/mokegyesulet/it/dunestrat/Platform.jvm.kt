package hu.mokegyesulet.it.dunestrat

import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.UrlLauncher
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

@OptIn(SupabaseExperimental::class)
actual fun getUrlLauncher(): UrlLauncher? = UrlLauncher { _, url ->
    val osName = System.getProperty("os.name").lowercase()
    val runtime = Runtime.getRuntime()
    if (osName.contains("win")) {
        runtime.exec(arrayOf("rundll32", "url.dll,FileProtocolHandler", url))
    } else if (osName.contains("mac")) {
        runtime.exec(arrayOf("open", url))
    } else {
        // Linux and other Unix-like
        val browsers =
            listOf(
                "xdg-open",
                "gio open",
                "gnome-open",
                "kde-open",
                "sensible-browser",
                "firefox",
                "google-chrome",
            )
        var opened = false
        for (browser in browsers) {
            try {
                val browserCmd = browser.split(" ")[0]
                // Using 'which' to check if command exists
                if (runtime.exec(arrayOf("which", browserCmd)).waitFor() == 0) {
                    runtime.exec(arrayOf(browserCmd, url))
                    opened = true
                    break
                }
            } catch (e: Exception) {
                // Ignore and try next
            }
        }
        if (!opened) {
            // Last resort: try just running the first one anyway
            try {
                runtime.exec(arrayOf("xdg-open", url))
            } catch (e: Exception) {
                // throw UnsupportedOperationException("Could not open browser for URL: $url. Error: ${e.message}")
            }
        }
    }
}
