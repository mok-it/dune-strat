package hu.mokegyesulet.it.dunestrat

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override fun openUrl(url: String) {
        openUrlNative(url)
    }
}

private fun openUrlNative(url: String): Unit = js("window.open(url, '_blank')")

actual fun getPlatform(): Platform = WasmPlatform()
