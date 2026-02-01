package hu.mokegyesulet.it.dunestrat.util

import hu.mokegyesulet.it.dunestrat.model.Desert

fun hexDrawer(unitVector: UnitVector): String {
    val transformVector = getTransformVector(unitVector)

    return "<polygon style =\"fill:none;stroke:#000000;stroke-width:2.5px\" points = " +

        "\"${148 + transformVector.x },${183.138438763306 + transformVector.y} " +
        "${52 + transformVector.x },${183.138438763306 + transformVector.y} " +
        "${4 + transformVector.x },${100 + transformVector.y} " +
        "${52 + transformVector.x },${16.8615612366939 + transformVector.y} " +
        "${148 + transformVector.x},${16.8615612366939 + transformVector.y } " +
        "${196 + transformVector.x},${100 + transformVector.y }\"/>"
}

fun getTransformVector(unitVector: UnitVector): AbsoluteVector {
    val vecX = AbsoluteVector(144.0, -83.13843876)
    val vecY = AbsoluteVector(0.0, 166.2768775)
    return unitVector.x * vecX + unitVector.y * vecY
}

fun getSimpleSvg(desert: Desert): String {
    val builder = StringBuilder()
    builder.append("<svg xmlns=\"http://www.w3.org/2000/svg\">\n")

    for (field in desert.fields) {
        val unitVector = UnitVector.fromDisplayCoordinate(field.id)
        val transformVector = getTransformVector(unitVector)
        builder.append(hexDrawer(unitVector) + "\n")
        builder.append(
            "<text x=\"${transformVector.x + 50}\" y=\"${transformVector.y + 50}\">${field.id}</text>\n",
        )
    }

    builder.append("</svg>")
    return builder.toString()
}

fun main() {
    val desert = Desert.create12PlayerHexagon()
    val svgString = getSimpleSvg(desert)
    println(svgString)
}
