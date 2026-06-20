package hu.mokegyesulet.it.dunestrat.util.drawmap

import java.awt.Desktop
import java.net.URI

actual fun openURL(url: String) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(url))
    }
}
