package hu.mokegyesulet.it.dunestrat.backend

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

object SupabaseAuth : Auth {
    private val auth = SupabaseClientProvider.client.auth

    override suspend fun logIn() = auth.signInWith(Google)
    override val sessionStatus get(): Flow<SessionStatus> = auth.sessionStatus

    override suspend fun logOut() = auth.signOut()
}
