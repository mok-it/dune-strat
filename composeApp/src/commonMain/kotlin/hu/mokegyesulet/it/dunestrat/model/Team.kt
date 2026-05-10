package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable

@Serializable
class Team(
    val playerId: Int,
    val students: List<Student>,
)
