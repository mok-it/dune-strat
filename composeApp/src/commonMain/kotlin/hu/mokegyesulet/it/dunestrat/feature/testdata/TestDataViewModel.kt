package hu.mokegyesulet.it.dunestrat.feature.testdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestDataViewModel : ViewModel() {

    val deserts = SupabaseRepository.getDeserts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val games = SupabaseRepository.getGames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gameStates = SupabaseRepository.getGameStates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var players = (1..12).map { initPlayer(it) }

    init {
        players = (0..11).map { initPlayer(it) }
        viewModelScope.launch {
            games.collect { list ->
                println("Games from Supabase: $list")
            }
        }
        viewModelScope.launch {
            deserts.collect { list ->
                println("Deserts from Supabase: $list")
            }
        }
        viewModelScope.launch {
            gameStates.collect { list ->
                println("Game states from Supabase: $list")
            }
        }
    }

    fun initDesert() {
        val test = Desert.create6PlayerTestHexagon()
        viewModelScope.launch {
            SupabaseRepository.saveDesert(test)
        }
    }

    fun initGame(desert: Desert) {
        val playerCount = desert.fields.filter { it.startingField }.size
        players = players.take(playerCount)
        val names = listOf(
            "Példa Aladár",
            "Példa Béla",
            "Példa Csanád",
            "Példa Dániel",
            "Példa Ezékiel",
            "Példa Ferenc",
            "Példa Gergely",
            "Példa Hilbert",
            "Példa Iván",
            "Példa Jakab",
            "Példa Károly",
            "Példa Lajos",
        )
        val teams = (0..<playerCount).map { i ->
            Team(players[i].id, listOf(Student(names[i])))
        }
        viewModelScope.launch {
            val game = Game(
                name = "Teszt játék",
                progress = GameProgress.INITIALIZED,
                teams = teams,
                desertId = desert.id,
            )
            SupabaseRepository.saveGame(game)
        }
    }

    fun initGameState(
        desert: Desert,
        game: Game,
    ) {
        val stateFields = desert.fields.toGameStateFields()
        randomlyAssignPlayers(desert, stateFields)
        val firstState = GameState(
            0,
            game.id,
            0,
            stateFields,
            players.toSet(),
        )
        viewModelScope.launch {
            SupabaseRepository.saveGameState(firstState)
        }
    }

    private fun initPlayer(id: Int): Player = Player(
        id,
        0,
        0,
        0,
        mutableMapOf(
            Weapon.CRYSKNIFE to 0,
            Weapon.PISTOL to 0,
            Weapon.LASGUN to 0,
            Weapon.LEGION to 0,
        ),
        mutableSetOf(),
    )
    private fun randomlyAssignPlayers(
        desert: Desert,
        fields: Set<GameStateField>,
    ) {
        val startingFields = desert.fields.filter { it.startingField }.map { it.id }.shuffled()
        if (startingFields.size != players.size) {
            throw RuntimeException(
                "Start field count and player count mismatched." +
                    " Players: ${players.size}, starting fields: ${startingFields.size} \n" +
                    "Starting fields: ${startingFields.joinToString(", ")}",
            )
        }
        players.zip(startingFields).forEach { (player, coordinate) ->
            player.ownedFields.add(fields.first { it.id == coordinate })
        }
    }
}
