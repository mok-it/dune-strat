package hu.mokegyesulet.it.dunestrat

interface Platform {
    val name: String
    fun openUrl(url: String)
}

expect fun getPlatform(): Platform
