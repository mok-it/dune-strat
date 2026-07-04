package hu.mokegyesulet.it.dunestrat.model

data class GameState(
    val id: Int = -1,
    val gameId: Int,
    var index: Int,
    val fields: Set<GameStateField>,
    val players: Set<Player>,
) {

    fun runTurn(playerSteps: MutableSet<PlayerStep>): GameState {
        this.leaveFields(playerSteps)
        this.waterConsumption(playerSteps)
        this.checkPurchases(playerSteps)
        this.buildHarvesters(playerSteps)
        this.purchaseWeapons(playerSteps)
        this.produceSpice()
        this.expansions(playerSteps)
        this.deliverWeapons(playerSteps)
        index++

        return GameState(
            gameId = gameId,
            index = index,
            fields = fields,
            players = players,
        )
    }

    fun runLastTurn(playerSteps: MutableSet<PlayerStep>): GameState {
        this.leaveFields(playerSteps)
        this.waterConsumption(playerSteps)
        this.checkPurchases(playerSteps)
        this.buildHarvesters(playerSteps)
        this.purchaseWeapons(playerSteps)
        this.expansions(playerSteps)
        this.produceSpice()
        this.deliverWeapons(playerSteps)
        index++

        return GameState(
            gameId = gameId,
            index = index,
            fields = fields,
            players = players,
        )
    }

    fun leaveFields(playerSteps: Set<PlayerStep>) {
        players.forEach { player ->
            player.leaveFields(getPlayerStepById(player.id, playerSteps).leaveFields)
        }
    }

    fun waterConsumption(playerSteps: MutableSet<PlayerStep>) {
        players.forEach { player ->
            if (player.waterConsumption()) {
                playerSteps.removeAll { it.playerId == player.id }
                playerSteps.add(
                    PlayerStep(
                        gameStateId = -1,
                        playerId = player.id,
                        leaveFields = setOf(),
                        enterFields = setOf(),
                        purchaseWeapons = mapOf(),
                        buildHarvesters = setOf(),
                    ),
                )
            }
        }
    }

    fun getPlayerStepById(
        playerId: Int,
        playerSteps: Set<PlayerStep>,
    ): PlayerStep = playerSteps.find { playerStep ->
        playerStep.playerId == playerId
    } ?: PlayerStep(
        gameStateId = -1,
        playerId = playerId,
        leaveFields = setOf(),
        enterFields = setOf(),
        purchaseWeapons = mapOf(),
        buildHarvesters = setOf(),
    )

    fun checkPurchases(playerSteps: Set<PlayerStep>) {
        players.forEach { player ->
            player.inDebt =
                player.spice < player.calculatePrices(getPlayerStepById(player.id, playerSteps))
        }
    }

    fun buildHarvesters(playerSteps: Set<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            getPlayerStepById(player.id, playerSteps).buildHarvesters.forEach { fieldId ->
                val field =
                    player.ownedFields.find { field -> field.id == fieldId } ?: return@forEach

                player.purchaseHarvester(field)
            }
        }
    }

    fun purchaseWeapons(playerSteps: Set<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            player.purchaseWeapons(getPlayerStepById(player.id, playerSteps).purchaseWeapons)
        }
    }

    fun produceSpice() {
        players.forEach { player ->
            player.ownedFields.forEach { field ->
                player.spice += field.spice
            }
        }
    }

    fun deliverWeapons(playerSteps: Set<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            player.deliverWeapons(getPlayerStepById(player.id, playerSteps).purchaseWeapons)
        }
    }

    fun expansions(playerSteps: Set<PlayerStep>) {
        val chosenFields = mutableMapOf<GameStateField, MutableSet<Player>>()
        val losses = mutableMapOf<Player, Double>()

        players.forEach { player ->
            losses[player] = 0.0
        }

        fields.forEach { field ->
            chosenFields[field] = mutableSetOf()
        }

        players.filter { !it.inDebt }
            .forEach { player ->
                getPlayerStepById(player.id, playerSteps).enterFields
                    .mapNotNull { fieldId ->
                        fields.find { field -> field.id == fieldId }
                    }
                    .filter { field ->
                        player.isFieldReachable(field)
                    }
                    .forEach { field -> chosenFields[field]?.add(player) }
            }

        chosenFields.filter { it.value.isNotEmpty() }
            .forEach { entry ->
                val field = entry.key
                val players = entry.value

                if (players.isNotEmpty()) {
                    val occupant = getOccupant(field)
                    val max = players.maxOfOrNull { it.calculatePower(field) } ?: 0
                    val winningPlayers: List<Player> = players.filter { player ->
                        player.calculatePower(field) == max
                    }
                    val newOccupant = modifyFieldOwnershipsAfterBattle(field, winningPlayers)
                    players.forEach { player ->
                        losses[player] =
                            (losses[player] ?: 0.0) +
                            calculateLossOnField(player, occupant, newOccupant)
                    }
                }
            }
        players.forEach { player ->
            player.weapons.forEach { weapon ->
                player.weapons[weapon.key] =
                    (player.weapons[weapon.key] ?: 0) -
                    ((losses[player] ?: 0.0) * (player.weapons[weapon.key] ?: 0)).toInt()
            }
        }
    }

    fun getOccupant(field: GameStateField): Player? {
        players.forEach { player ->
            if (field in player.ownedFields) {
                return player
            }
        }
        return null
    }

    private fun modifyFieldOwnershipsAfterBattle(
        field: GameStateField,
        winningPlayers: List<Player>,
    ): Player? {
        val occupant = getOccupant(field)
        if (occupant in winningPlayers) {
            return occupant
        }
        occupant?.ownedFields?.remove(field)
        if (winningPlayers.size >= 2) {
            return null
        }
        val newOccupant = winningPlayers[0]
        newOccupant.ownedFields.add(field)
        return newOccupant
    }

    private fun calculateLossOnField(
        player: Player,
        previousOccupant: Player?,
        newOccupant: Player?,
    ): Double {
        if (previousOccupant == player) {
            return 0.0
        }
        if (newOccupant == player) {
            return 0.2
        }
        return 0.1
    }
}
