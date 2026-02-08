package hu.mokegyesulet.it.dunestrat.util

import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.model.DesertField

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
    var minX = 0.0
    var maxX = 0.0
    var minY = 0.0
    var maxY = 0.0

    val fieldSvgs = desert.fields.map { field ->
        val unitVector = UnitVector.fromDisplayCoordinate(field.id)
        val transformVector = getTransformVector(unitVector)

        minX = minOf(minX, transformVector.x)
        maxX = maxOf(maxX, transformVector.x)
        minY = minOf(minY, transformVector.y)
        maxY = maxOf(maxY, transformVector.y)

        val hex = hexDrawer(unitVector) + "\n"

        val text = getFieldText(field, transformVector)
        hex + text
    }

    val width = maxX - minX + 300
    val height = maxY - minY + 300

    val viewbox = "viewbox = \"${minX - 100} ${minY - 100} $width $height\""

    val svgOpening = "<svg width=\"$width\" height=\"$height\" $viewbox " +
        "xmlns=\"http://www.w3.org/2000/svg\">\n"
    val svgEnd = "</svg>"
    return svgOpening + fieldSvgs.joinToString(separator = "") { it } + svgEnd
}

fun getFieldText(field: DesertField, transformVector: AbsoluteVector): String {
    val textStart = "<text x=\"${transformVector.x + 50}\" y=\"${transformVector.y + 50}\">"
    val textId = field.id
    val textValues = "(${field.water}, ${field.spice})"
    val textEnd = "</text>\n"
    return textStart + textId + textValues + textEnd
}

fun main() {
    val desert = Desert.create12PlayerHexagon()
    val svgString = getSimpleSvg(desert)
    println(svgString)
}
