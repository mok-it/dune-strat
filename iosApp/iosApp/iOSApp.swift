import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Initialize Napier logging for iOS
        NapierProxyKt.debugBuild()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}