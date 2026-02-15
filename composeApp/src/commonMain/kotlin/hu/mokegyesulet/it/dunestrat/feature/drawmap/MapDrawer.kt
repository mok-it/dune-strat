package hu.mokegyesulet.it.dunestrat.feature.drawmap

import hu.mokegyesulet.it.dunestrat.model.Desert
import kotlin.math.PI

object MapDrawer {

    private const val HEXAGON_SIZE = 200.0
    private const val INNER_HEXAGON_RATIO = 0.65

    private const val STYLE = "style=\"fill:none;stroke:#000000;stroke-width:2.5px\""

    private val distanceToVertex = Vector2D(HEXAGON_SIZE / 2, 0.0)

    private val hexagonVertices = (0..5).map {
        distanceToVertex.rotate(PI / 3 * it)
    }

    private val innerHexagonVertices = hexagonVertices.map { it * INNER_HEXAGON_RATIO }

    private val numberOffsetVector = hexagonVertices[1] - hexagonVertices[5]
    private val letterOffsetVector2D = (hexagonVertices[0] - hexagonVertices[4]).invertY()

    private fun getOffsetVector(coordinate: String): Vector2D {
        val letterOffset = coordinate.lowercase()[0].code - 'a'.code
        val numberOffset = coordinate.substring(1).toInt() - 1
        return letterOffsetVector2D * letterOffset + numberOffsetVector * numberOffset
    }

    private fun getHexagonSvg(offset: Vector2D): String {
        val points = hexagonVertices.map { it + offset }
        return polygonSvgFromPoints(points)
    }

    private fun getFieldTextSvg(
        fieldId: String,
        offset: Vector2D,
    ): String {
        val textStart = "<text x=\"${offset.x}\" y=\"${offset.y}\">"
        return "$textStart$fieldId</text>\n"
    }

    private fun polygonSvgFromPoints(points: List<Vector2D>): String {
        val pointsString = points.joinToString(separator = " ") { "${it.x},${it.y}" }
        return "<polygon $STYLE points=\"$pointsString\"/>\n"
    }

    private fun getWeaponBoxSvg(offset: Vector2D): String {
        val points = listOf(
            hexagonVertices[1],
            innerHexagonVertices[1],
            innerHexagonVertices[2],
            hexagonVertices[2],
        ).map { it + offset }
        return polygonSvgFromPoints(points)
    }

    private fun getWaterBoxSvg(offset: Vector2D): String {
        val points = listOf(
            hexagonVertices[2],
            innerHexagonVertices[2],
            innerHexagonVertices[3],
            hexagonVertices[3],
        ).map { it + offset }
        return polygonSvgFromPoints(points)
    }

    private fun getSpiceBoxSvg(offset: Vector2D): String {
        val points = listOf(
            hexagonVertices[0],
            innerHexagonVertices[0],
            innerHexagonVertices[1],
            hexagonVertices[1],
        ).map { it + offset }
        return polygonSvgFromPoints(points)
    }

    private fun getCoordinateCircleSvg(offset: Vector2D): String {
        val center = (hexagonVertices[4] + hexagonVertices[5]) / 2 + offset
        val radius = (HEXAGON_SIZE * 3) / 16
        val d = "M ${center.x - radius} ${center.y} " +
            "A $radius $radius 0 0 0 " +
            "${center.x + radius} ${center.y}"
        return "<path d=\"$d\" $STYLE />\n"
    }

    fun getDesertSvg(desert: Desert): String {
        var minX = 0.0
        var maxX = 0.0
        var minY = 0.0
        var maxY = 0.0

        val fieldSvgs = desert.fields.map { field ->
            val offset = getOffsetVector(field.id)

            minX = minOf(minX, offset.x)
            maxX = maxOf(maxX, offset.x)
            minY = minOf(minY, offset.y)
            maxY = maxOf(maxY, offset.y)

            val hex = getHexagonSvg(offset)
            val text = getFieldTextSvg(field.id, offset)
            val weaponBox = getWeaponBoxSvg(offset)
            val coordinateCircle = getCoordinateCircleSvg(offset)
            val waterBox = getWaterBoxSvg(offset)
            val spiceBox = getSpiceBoxSvg(offset)

            hex + text + weaponBox + coordinateCircle + waterBox + spiceBox
        }

        val width = maxX - minX + 300
        val height = maxY - minY + 300

        val viewbox = "viewbox = \"${minX - 150} ${minY - 150} $width $height\""

        val svgOpening = "<svg width=\"$width\" height=\"$height\" $viewbox " +
            "xmlns=\"http://www.w3.org/2000/svg\">\n"
        val svgEnd = "</svg>"
        return svgOpening + fieldSvgs.joinToString(separator = "") { it } + svgEnd
    }
}

fun main() {
    val desert = Desert.create12PlayerHexagon()
    println(MapDrawer.getDesertSvg(desert))
}
