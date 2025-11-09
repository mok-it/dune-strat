package hu.mokegyesulet.it.dunestrat.model

data class Desert(
    val fields: Set<DesertField>,
) {
    companion object {
        fun create12PlayerHexagon(): Desert {
            TODO()
        }
    }
}
