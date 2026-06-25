package hu.mokegyesulet.it.dunestrat.backend

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SupabaseAuth : Auth {
    private val auth = SupabaseClientProvider.client.auth

    override suspend fun logIn() =
        auth.signInWith(provider = Google, redirectUrl = "https://mok-it.github.io/dune-strat/")
    override val authStatus get(): Flow<AuthStatus> = auth.sessionStatus.map { s ->
        if (s is SessionStatus.Authenticated) {
            AuthStatus.Authenticated(
                s.session.user?.email,
            )
        } else {
            AuthStatus.Unauthenticated
        }
    }

    override suspend fun logOut() = auth.signOut()
}
