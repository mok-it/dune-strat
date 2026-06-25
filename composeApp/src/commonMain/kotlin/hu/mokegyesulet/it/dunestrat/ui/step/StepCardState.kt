package hu.mokegyesulet.it.dunestrat.ui.step

import hu.mokegyesulet.it.dunestrat.model.Weapon

data class StepCardState(
    val playerId: Int,
    val leaveFields: String = "",
    val enterFields: String = "",
    val purchaseWeapons: Map<Weapon, Int> = mapOf(
        Weapon.LASGUN to 0,
        Weapon.PISTOL to 0,
        Weapon.CRYSKNIFE to 0,
        Weapon.LEGION to 0,
    ),
    val buildHarvesters: String = "",
)
sealed class StepCardEvent {
    class LeaveFieldsChanged(val leaveFields: String) : StepCardEvent()
    class EnterFieldsChanged(val enterFields: String) : StepCardEvent()
    class PurchaseWeaponsChanged(val weaponType: Weapon, val amount: Int) : StepCardEvent()
    class BuildHarvestersChanged(val buildHarvesters: String) : StepCardEvent()
}
