package hu.mokegyesulet.it.dunestrat

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

/**
 * Initializes Napier logging with DebugAntilog.
 * Call this during app startup on each platform to enable logging.
 *
 * On iOS (Swift), call: NapierProxyKt.debugBuild()
 */
fun debugBuild(coroutinesSuffix: Boolean = false) {
    // Some Napier versions don't support the coroutinesSuffix parameter on DebugAntilog.
    // We accept the parameter for API parity but ignore it if unavailable.
    Napier.base(DebugAntilog())
}
