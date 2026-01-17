package hu.mokegyesulet.it.dunestrat.backend

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClientProvider {
    private val _client = createSupabaseClient(
        supabaseUrl = "https://lnwvuwepwaexwybselsf.supabase.co",
        supabaseKey = "sb_publishable_Y8MlS_tbyIRvvsvbHvb1_g_wbyg6EfI",
    ) {
        install(io.github.jan.supabase.auth.Auth)
        install(Postgrest)
        install(Realtime)
    }
    val client get() = _client
}
