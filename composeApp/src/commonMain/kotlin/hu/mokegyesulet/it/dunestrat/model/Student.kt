package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable

@Serializable
class Student(
    val name: String = "Ezekiel",
    val batkabankId: String = "",
) {
    fun copy(
        name: String = this.name,
        batkabankId: String = this.batkabankId,
    ) = Student(name, batkabankId)
}
