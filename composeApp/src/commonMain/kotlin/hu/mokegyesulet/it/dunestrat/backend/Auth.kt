package hu.mokegyesulet.it.dunestrat.backend

import kotlinx.coroutines.flow.Flow

interface Auth {
    suspend fun logIn()
    suspend fun logOut()
    val authStatus: Flow<AuthStatus>
}
