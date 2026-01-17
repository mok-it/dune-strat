package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable

@Serializable
class Team(
    val playerId: String,
    val students: List<Student>,
)
