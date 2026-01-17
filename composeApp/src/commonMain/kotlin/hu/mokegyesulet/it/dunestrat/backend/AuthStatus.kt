package hu.mokegyesulet.it.dunestrat.backend

sealed class AuthStatus {
    class Authenticated(email: String?) : AuthStatus() {
        val email: String? = email
    }
    object Unauthenticated : AuthStatus()
}
