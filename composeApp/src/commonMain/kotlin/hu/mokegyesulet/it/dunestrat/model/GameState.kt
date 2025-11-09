package hu.mokegyesulet.it.dunestrat.model

data class GameState(
    val fields: Set<GameStateField>,
    val players: Set<Player>,
) {

    fun runTurn(playerSteps: Set<PlayerStep>): GameState {
        val newFields = mutableSetOf<GameStateField>()
        val newPlayers = players.toMutableSet()
        val playerSteps = playerSteps.toMutableSet()

        this.leaveFields(newPlayers, playerSteps)
        this.waterConsumption(newPlayers, playerSteps)



        return this.copy(fields = newFields, players = newPlayers)
    }


    fun leaveFields(players: Set<Player>, playerSteps: Set<PlayerStep>) {
        players.map { player ->
            player.leaveFields(getPlayerStepById(player.id, playerSteps).leaveFields)
        }
    }

    fun waterConsumption(players: Set<Player>, playerSteps: Set<PlayerStep>): Set<PlayerStep> {
        var newPlayerSteps = playerSteps.toMutableSet()
        players.map { player ->
            val res = player.waterConsumption()
            if (res.second) {
                newPlayerSteps = newPlayerSteps.filter { playerStep -> playerStep.playerId == player.id }.toMutableSet()
                newPlayerSteps.add(PlayerStep(player.id, setOf(), setOf(), mapOf(), setOf()))
            }
            res.first
        }
        return newPlayerSteps
    }

    fun getPlayerStepById(playerId: String, playerSteps: Set<PlayerStep>): PlayerStep {
        return playerSteps.first() { playerStep -> playerStep.playerId == playerId }
    }
}
