package hu.mokegyesulet.it.dunestrat.backend

interface Auth {
    suspend fun logIn()
    suspend fun logOut()
}
