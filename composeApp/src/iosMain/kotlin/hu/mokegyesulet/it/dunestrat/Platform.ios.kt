package hu.mokegyesulet.it.dunestrat

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override fun openUrl(url: String) {
        // Handled natively
    }
}


actual fun getPlatform(): Platform = IOSPlatform()
