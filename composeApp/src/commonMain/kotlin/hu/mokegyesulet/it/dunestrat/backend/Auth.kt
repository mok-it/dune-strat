package hu.mokegyesulet.it.dunestrat.backend

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface Auth {
    suspend fun logIn()
    suspend fun logOut()
    val sessionStatus: Flow<SessionStatus>
}
