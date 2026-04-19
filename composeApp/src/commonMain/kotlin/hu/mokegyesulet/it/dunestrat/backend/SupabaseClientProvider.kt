package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.getUrlLauncher
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClientProvider {
    @OptIn(SupabaseExperimental::class)
    private val _client = createSupabaseClient(
        supabaseUrl = "https://lnwvuwepwaexwybselsf.supabase.co",
        supabaseKey = "sb_publishable_Y8MlS_tbyIRvvsvbHvb1_g_wbyg6EfI",
    ) {
        install(io.github.jan.supabase.auth.Auth) {
            getUrlLauncher()?.let {
                urlLauncher = it
            }
        }
        install(Postgrest)
        install(Realtime)
    }
    val client get() = _client
}
