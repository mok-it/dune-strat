package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.getPlatform
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.UrlLauncher
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.annotations.SupabaseExperimental

object SupabaseClientProvider {
    @OptIn(SupabaseExperimental::class)
    private val _client = createSupabaseClient(
        supabaseUrl = "https://lnwvuwepwaexwybselsf.supabase.co",
        supabaseKey = "sb_publishable_Y8MlS_tbyIRvvsvbHvb1_g_wbyg6EfI",
    ) {
        install(Auth) {
            urlLauncher = object : UrlLauncher {
                override suspend fun openUrl(supabase: SupabaseClient, url: String) {
                    getPlatform().openUrl(url)
                }
            }
        }
        install(Postgrest)
        install(Realtime)
    }
    val client get() = _client
}
