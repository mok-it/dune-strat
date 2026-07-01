package hu.mokegyesulet.it.dunestrat.backend

object SupabaseConfig {
    const val ANON_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZS1kZW1vIiwicm9sZSI6ImFub24iLCJleHAiOjE5ODM4MTI5OTZ9.CRXP1A7WOeoJeXxjNni43kdQwgnWNReilDMblYTn_I0"

    val apiUrl: String
        get() = getSupabaseApiUrl()

    fun publicStorageObjectUrl(
        bucket: String,
        path: String,
    ): String = "${apiUrl.trimEnd('/')}/storage/v1/object/public/$bucket/$path"
}

expect fun getSupabaseApiUrl(): String
