package hu.mokegyesulet.it.dunestrat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform