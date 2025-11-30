package hu.mokegyesulet.it.dunestrat.util

fun hexDrawer(
    absoluteVector: UnitVector,
    newVecX: AbsoluteVector,
    newVecY: AbsoluteVector,
): String {
    val vecX = AbsoluteVector(144.0, -83.13843876)
    val vecY = AbsoluteVector(0.0, 166.2768775)

    val transformVector = absoluteVector.x * vecX + absoluteVector.y * vecY

    return "<polygon style =\"fill:none;stroke:#000000;stroke-width:2.5px\" points = " +

        "\"${148 + transformVector.y },${183.138438763306 + transformVector.x } " +
        "${52 + transformVector.y },${183.138438763306 + transformVector.x} " +
        "${4 + transformVector.y },${100 + transformVector.x} " +
        "${52 + transformVector.y },${16.8615612366939 + transformVector.x} " +
        "${148 + transformVector.y},${16.8615612366939 + transformVector.x } " +
        "${196 + transformVector.y},${100 + transformVector.x }\"/>"
}
