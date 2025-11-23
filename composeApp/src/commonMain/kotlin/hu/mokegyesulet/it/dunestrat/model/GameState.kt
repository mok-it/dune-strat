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
        this.buildHarvesters(playerSteps)
        this.purchaseWeapons(playerSteps)
        this.produce()
        this.expansions(playerSteps)
        this.deliverWeapons(playerSteps)
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
            player.inDebt =
                player.spice >= player.calculatePrices(getPlayerStepById(player.id, playerSteps))
        }
    }

    fun buildHarvesters(playerSteps: MutableSet<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            getPlayerStepById(player.id, playerSteps).buildHarvesters.forEach { field ->
                if (field in player.ownedFields) {
                    player.purchaseHarvester(field)
                }
            }
        }
    }

    fun purchaseWeapons(playerSteps: MutableSet<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            player.purchaseWeapons(getPlayerStepById(player.id, playerSteps).purchaseWeapons)
        }
    }

    fun produceResources() {
        players.forEach { player ->
            player.ownedFields.forEach { field ->
                player.spice += field.spice
                player.water += field.water
            }
        }
    }

    fun deliverWeapons(playerSteps: MutableSet<PlayerStep>) {
        players.filter { !it.inDebt }.forEach { player ->
            player.deliverWeapons(getPlayerStepById(player.id, playerSteps).purchaseWeapons)
        }
    }

    fun expansions(playerSteps: MutableSet<PlayerStep>) {
        val chosenfields = mutableMapOf<GameStateField, MutableSet<Player>>()
        val losses = mutableMapOf<Player, Double>()

        players.forEach { player ->
            losses[player] = 0.0
        }

        fields.forEach { field ->
            chosenfields[field] = mutableSetOf()
        }

        players.filter { !it.inDebt }.forEach { player ->
            getPlayerStepById(player.id, playerSteps).enterFields.forEach { field ->
                if (player.isFieldReachable(field)) {
                    chosenfields[field]?.add(player)
                }
            }
        }

        chosenfields.forEach { entry ->
            val field = entry.key
            val players = entry.value

            if (players.isNotEmpty()) {
                var max = players.first().calculatePower(field)
                var winingplayer: Player = players.first()
                var draw = false
                players.filter { player -> player != winingplayer }.forEach { player ->
                    val power = player.calculatePower(field)
                    if (max < power) {
                        max = power
                        winingplayer = player
                        draw = false
                    } else if (max == power) {
                        draw = true
                    }
                    if (fieldOcupiedBy(field) != null) {
                        if (fieldOcupiedBy(field)!!.calculatePower(field) >= max) {
                            draw = true
                        } else if (draw == true) {
                            player.leaveFields(setOf(field))
                        }
                    } else if (!draw) {
                        winingplayer.ownedFields.add(field)
                        losses[winingplayer] = losses[winingplayer]!! + 0.2
                    } else {
                        losses[winingplayer] = losses[winingplayer]!! + 0.1
                    }
                    players.filter { player -> player != winingplayer }.forEach { player ->
                        losses[player] = losses[player]!! + 0.1
                    }
                }
            }
        }

        players.forEach { player ->
            if (losses[player]!! > 1.0) {
                losses[player] = 1.0
            }
            player.loseWeaponPrecent(losses[player]!!)
        }
    }

    fun fieldOcupiedBy(field: GameStateField): Player? {
        players.forEach { player ->
            if (field in player.ownedFields) {
                return player
            }
        }
        return null
    }
}
