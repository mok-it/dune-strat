package hu.mokegyesulet.it.dunestrat

import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.UrlLauncher

interface Platform {
    val name: String
    fun openUrl(url: String)
}

expect fun getPlatform(): Platform

@OptIn(SupabaseExperimental::class)
expect fun getUrlLauncher(): UrlLauncher?
