package hu.mokegyesulet.it.dunestrat.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openURL(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}
