package hu.mokegyesulet.it.dunestrat.model

data class GameState(
    val fields: Set<GameStateField>,
    val players: Set<Player>,
) {

    fun runTurn(playerSteps: Set<PlayerStep>): GameState {
        val newFields = fields.toMutableSet()
        val newPlayers = players.toMutableSet()

        TODO()
    }
}
