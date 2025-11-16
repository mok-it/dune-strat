package hu.mokegyesulet.it.dunestrat.model

class GameState(
    val fields: Set<GameStateField>,
    val players: Set<Player>,
) {

    fun runTurn(playerSteps: MutableSet<PlayerStep>) {
        val playerSteps = playerSteps.toMutableSet()

        this.leaveFields(playerSteps)
        this.waterConsumption(playerSteps)
        this.checkPurchases(playerSteps)
        // TODO: implement combat
    }

    fun leaveFields(playerSteps: Set<PlayerStep>) {
        players.map { player ->
            player.leaveFields(getPlayerStepById(player.id, playerSteps).leaveFields)
        }
    }

    fun waterConsumption(playerSteps: MutableSet<PlayerStep>) {
        players.forEach { player ->
            val res = player.waterConsumption()
            if (res) {
                playerSteps.removeAll { it.playerId == player.id }
                playerSteps.add(PlayerStep(player.id, setOf(), setOf(), mapOf(), setOf()))
            }
        }
    }

    fun getPlayerStepById(playerId: String, playerSteps: Set<PlayerStep>): PlayerStep =
        playerSteps.first { playerStep ->
            playerStep.playerId == playerId
        }

    fun checkPurchases(playerSteps: MutableSet<PlayerStep>) {
        players.forEach { player ->
            player.inDebt = player.validatePrices(
                player.calculatePrices(getPlayerStepById(player.id, playerSteps)),
            )
        }
    }

    fun purchaseHarvesers(playerSteps: MutableSet<PlayerStep>) {
        players.forEach { player ->
            if (!player.inDebt) {
                getPlayerStepById(player.id, playerSteps).buildHarvester.forEach { field ->
                    player.purchaseHarvester(field)
                }
            }
        }
    }
}
