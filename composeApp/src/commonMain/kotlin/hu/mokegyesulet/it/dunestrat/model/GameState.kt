package hu.mokegyesulet.it.dunestrat.model

data class GameState(
    val id: Int = -1,
    val gameId: Int,
    var index: Int,
    val fields: Set<GameStateField>,
    val players: Set<Player>,
) {

    /**
     * Executes a full turn and applies all step phases in order.
     *
     * @param playerSteps the mutable set of player actions for the turn
     * @return a new [GameState] snapshot after the turn is completed
     */
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

    /**
     * Executes the final turn, switching the order of the expansions and the spice production.
     *
     * @param playerSteps the mutable set of player actions for the turn
     * @return a new [GameState] snapshot after the last turn is completed
     */
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

    /**
     * Applies the fields each player is leaving for the current turn.
     *
     * @param playerSteps the set of player actions for the turn
     */
    fun leaveFields(playerSteps: Set<PlayerStep>) {
        players.forEach { player ->
            player.leaveFields(getPlayerStepById(player.id, playerSteps).leaveFields)
        }
    }

    /**
     * Consumes water for all players and resets the action set when a player runs out.
     *
     * @param playerSteps the mutable set of player actions for the turn
     */
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

    /**
     * Finds the action record for the given player or returns an empty default step.
     *
     * @param playerId the player identifier to search for
     * @param playerSteps the available player actions
     * @return the matching [PlayerStep], or an empty default step if none exists
     */
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

    /**
     * Marks each player as being in debt when their spice is below the cost of their planned purchases.
     *
     * @param playerSteps the set of player actions for the turn
     */
    fun checkPurchases(playerSteps: Set<PlayerStep>) {
        players.forEach { player ->
            player.inDebt =
                player.spice < player.calculatePrices(getPlayerStepById(player.id, playerSteps))
        }
    }

    /**
     * Builds harvesters for all non-indebted players on the fields they selected.
     *
     * @param playerSteps the set of player actions for the turn
     */
    fun buildHarvesters(playerSteps: Set<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            getPlayerStepById(player.id, playerSteps).buildHarvesters.forEach { fieldId ->
                val field =
                    player.ownedFields.find { field -> field.id == fieldId } ?: return@forEach

                player.purchaseHarvester(field)
            }
        }
    }

    /**
     * Processes weapon purchases for all players who are not in debt.
     *
     * @param playerSteps the set of player actions for the turn
     */
    fun purchaseWeapons(playerSteps: Set<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            player.purchaseWeapons(getPlayerStepById(player.id, playerSteps).purchaseWeapons)
        }
    }

    /**
     * Produces spice from every field owned by every player.
     */
    fun produceSpice() {
        players.forEach { player ->
            player.ownedFields.forEach { field ->
                player.spice += field.spice
            }
        }
    }

    /**
     * Delivers weapons for all players who are not in debt.
     *
     * @param playerSteps the set of player actions for the turn
     */
    fun deliverWeapons(playerSteps: Set<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            player.deliverWeapons(getPlayerStepById(player.id, playerSteps).purchaseWeapons)
        }
    }

    /**
     * Resolves field expansion battles and applies weapon losses from the outcomes.
     *
     * @param playerSteps the set of player actions for the turn
     */
    fun expansions(playerSteps: Set<PlayerStep>) {
        val chosenFields = mutableMapOf<GameStateField, MutableSet<Player>>()
        val losses = mutableMapOf<Player, Double>()

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

                val occupant = getOccupant(field)
                if (occupant != null) {
                    players.add(occupant)
                }
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
        players.forEach { player ->
            player.loseWeaponPrecent(losses[player] ?: 0.0)
        }
    }

    /**
     * Returns the current occupant of a field, if any.
     *
     * @param field the field whose owner should be resolved
     * @return the player owning the field, or `null` if it is unoccupied
     */
    fun getOccupant(field: GameStateField): Player? {
        players.forEach { player ->
            if (field in player.ownedFields) {
                return player
            }
        }
        return null
    }

    /**
     * Updates field ownership after a battle and returns the resulting occupant.
     *
     * @param field the contested field
     * @param winningPlayers the players who tied for the best battle power
     * @return the new owner of the field, or `null` if it remains unoccupied
     */
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

    /**
     * Calculates the loss factor for a player on a contested field.
     *
     * @param player the player whose losses are being computed
     * @param previousOccupant the field occupant before the battle
     * @param newOccupant the field occupant after the battle
     * @return the loss factor to apply to the player's weapons
     */
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
