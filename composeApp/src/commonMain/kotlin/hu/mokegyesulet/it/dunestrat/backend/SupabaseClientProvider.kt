package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.getUrlLauncher
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {
    @OptIn(SupabaseExperimental::class)
    private val _client = createSupabaseClient(
        supabaseUrl = SupabaseConfig.apiUrl,
        supabaseKey = SupabaseConfig.ANON_KEY,
    ) {
        install(io.github.jan.supabase.auth.Auth) {
            getUrlLauncher()?.let {
                urlLauncher = it
            }
        }
        install(Postgrest)
        install(Realtime)
        install(Storage)
    }
    val client get() = _client
}
