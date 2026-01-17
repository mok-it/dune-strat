package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable

@Serializable
class Student(
    val name: String = "Ezekiel",
    val batkabankId: String = "",
)
