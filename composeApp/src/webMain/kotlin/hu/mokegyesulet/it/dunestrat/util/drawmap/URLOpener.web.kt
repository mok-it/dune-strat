package hu.mokegyesulet.it.dunestrat.util.drawmap

import kotlinx.browser.window

actual fun openURL(url: String) {
    window.open(url, "_blank")
}
