package hu.mokegyesulet.it.dunestrat.util

fun hexDrawer(unitVector: UnitVector): String {
    val vecX = AbsoluteVector(144.0, -83.13843876)
    val vecY = AbsoluteVector(0.0, 166.2768775)

    val transformVector = unitVector.x * vecX + unitVector.y * vecY

    return "<polygon style =\"fill:none;stroke:#000000;stroke-width:2.5px\" points = " +

        "\"${148 + transformVector.x },${183.138438763306 + transformVector.y} " +
        "${52 + transformVector.x },${183.138438763306 + transformVector.y} " +
        "${4 + transformVector.x },${100 + transformVector.y} " +
        "${52 + transformVector.x },${16.8615612366939 + transformVector.y} " +
        "${148 + transformVector.x},${16.8615612366939 + transformVector.y } " +
        "${196 + transformVector.x},${100 + transformVector.y }\"/>"
}
