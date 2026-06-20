package hu.mokegyesulet.it.dunestrat.util.drawmap

import dune_strat.composeapp.generated.resources.Res
import hu.mokegyesulet.it.dunestrat.model.*
import kotlin.math.PI

object MapDrawer {

    private const val HEXAGON_SIZE = 200.0
    private const val INNER_HEXAGON_RATIO = 0.65

    private const val POLYGON_STYLE = "style=\"fill:none;stroke:#000000;stroke-width:2.5px\""
    private const val BOX_STYLE =
        "style=\"fill:#DDDDDD;fill-opacity:0.8;stroke:#000000;stroke-width:2.5px\""

    private val distanceToVertex = Vector2D(HEXAGON_SIZE / 2, 0.0)

    private val hexagonVertices = (0..5).map {
        distanceToVertex.rotate(PI / 3 * it)
    }

    private val innerHexagonVertices = hexagonVertices.map { it * INNER_HEXAGON_RATIO }

    private val numberOffsetVector = hexagonVertices[1] - hexagonVertices[5]
    private val letterOffsetVector2D = (hexagonVertices[0] - hexagonVertices[4]).invertY()

    private lateinit var desertSVG: String
    private lateinit var mountainSVG: String

    private lateinit var pistolSVG: String
    private lateinit var lasgunSVG: String
    private lateinit var crysknifeSVG: String

    private lateinit var waterSVG: String
    private lateinit var spiceSVG: String

    private var resourcesLoaded = false

    suspend fun loadResources(blackAndWhite: Boolean = false) {
        if (resourcesLoaded) {
            return
        }

        val desertBytes = if (blackAndWhite) {
            Res.readBytes("drawable/svg/desert_bnw.svg")
        } else {
            Res.readBytes("drawable/svg/desert.svg")
        }
        desertSVG = desertBytes.decodeToString().replace("cls-", "desert-cls-") + "\n"

        val mountainBytes = if (blackAndWhite) {
            Res.readBytes("drawable/svg/mountain_bnw.svg")
        } else {
            Res.readBytes("drawable/svg/mountain.svg")
        }
        mountainSVG = mountainBytes.decodeToString().replace("cls-", "mountain-cls-") + "\n"

        val pistolBytes = Res.readBytes("drawable/svg/pistol.svg")
        pistolSVG = pistolBytes.decodeToString().replace("cls-", "pistol-cls-") + "\n"

        val lasgunBytes = Res.readBytes("drawable/svg/lasgun.svg")
        lasgunSVG = lasgunBytes.decodeToString().replace("cls-", "lasgun-cls-") + "\n"

        val crysknifeBytes = Res.readBytes("drawable/svg/crysknife.svg")
        crysknifeSVG = crysknifeBytes.decodeToString().replace("cls-", "crysknife-cls-") + "\n"

        val waterBytes = Res.readBytes("drawable/svg/water.svg")
        waterSVG = waterBytes.decodeToString().replace("cls-", "water-cls-") + "\n"

        val spiceBytes = Res.readBytes("drawable/svg/spice.svg")
        spiceSVG = spiceBytes.decodeToString().replace("cls-", "spice-cls-") + "\n"

        resourcesLoaded = true
    }

    private fun getDefinitions(): String {
        val builder = StringBuilder()
        builder.append("<defs>\n")

        builder.append("<g id=\"desert\" transform=\"scale(0.6660724856)\">\n")
        builder.append(desertSVG)
        builder.append("</g>\n")

        builder.append("<g id=\"mountain\" transform=\"scale(0.6660724856)\">\n")
        builder.append(mountainSVG)
        builder.append("</g>\n")

        builder.append("<g id=\"pistol\">\n")
        builder.append(pistolSVG)
        builder.append("</g>\n")

        builder.append("<g id=\"lasgun\">\n")
        builder.append(lasgunSVG)
        builder.append("</g>\n")

        builder.append("<g id=\"crysknife\">\n")
        builder.append(crysknifeSVG)
        builder.append("</g>\n")

        builder.append("</defs>\n")
        return builder.toString()
    }

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
        val center =
            ((hexagonVertices[4] + hexagonVertices[5]) / 2) + Vector2D(0.0, 20.0) + offset
        val position = "x=\"${center.x}\" y=\"${center.y}\""
        val font = "font-size=\"20\" font-family=\"Times New Roman\""
        val align = "dominant-baseline=\"middle\" text-anchor=\"middle\""
        return "<text $position $align $font>$fieldId</text>\n"
    }

    private fun polygonSvgFromPoints(points: List<Vector2D>): String {
        val pointsString = points.joinToString(separator = " ") { "${it.x},${it.y}" }
        return "<polygon $POLYGON_STYLE points=\"$pointsString\"/>\n"
    }

    private fun boxSvgFromPoints(points: List<Vector2D>): String {
        val pointsString = points.joinToString(separator = " ") { "${it.x},${it.y}" }
        val style = "style=\"fill:#DDDDDD;fill-opacity:0.8;stroke:#000000;stroke-width:2.5px\""
        return "<polygon $style points=\"$pointsString\"/>\n"
    }

    private fun getWeaponBoxSvg(offset: Vector2D): String {
        val points = listOf(
            hexagonVertices[1],
            innerHexagonVertices[1],
            innerHexagonVertices[2],
            hexagonVertices[2],
        ).map { it + offset }
        return boxSvgFromPoints(points)
    }

    private fun getWaterBoxSvg(offset: Vector2D): String {
        val points = listOf(
            hexagonVertices[2],
            innerHexagonVertices[2],
            innerHexagonVertices[3],
            hexagonVertices[3],
        ).map { it + offset }
        return boxSvgFromPoints(points)
    }

    private fun getSpiceBoxSvg(offset: Vector2D): String {
        val points = listOf(
            hexagonVertices[0],
            innerHexagonVertices[0],
            innerHexagonVertices[1],
            hexagonVertices[1],
        ).map { it + offset }
        return boxSvgFromPoints(points)
    }

    private fun getCoordinateCircleSvg(offset: Vector2D): String {
        val center = (hexagonVertices[4] + hexagonVertices[5]) / 2 + offset
        val radius = (HEXAGON_SIZE * 3) / 16
        val d = "M ${center.x - radius} ${center.y} " +
            "A $radius $radius 0 0 0 " +
            "${center.x + radius} ${center.y}"
        return "<path d=\"$d\" $BOX_STYLE />\n"
    }

    private val backgroundOffset =
        hexagonVertices[3] - hexagonVertices[2] + (hexagonVertices[2] - hexagonVertices[1]) / 2

    private fun getBackgroundSvg(
        desert: Boolean,
        offset: Vector2D,
    ): String {
        val fullOffset = offset + backgroundOffset
        if (desert) {
            return "<use href=\"#desert\" x=\"${fullOffset.x}\" y=\"${fullOffset.y}\" />\n"
        }
        return "<use href=\"#mountain\" x=\"${fullOffset.x}\" y=\"${fullOffset.y}\" />\n"
    }

    private fun getWaterSvg(
        value: Int,
        offset: Vector2D,
    ): String {
        val midpoint = (hexagonVertices[1] + innerHexagonVertices[2]) / 2
        val totalOffset = offset + midpoint

        val font = "font-size=\"20\" font-family=\"Times New Roman\""
        val align = "dominant-baseline=\"middle\" text-anchor=\"middle\""

        val rotate = "rotate(60,${offset.x},${offset.y})"
        val translate = "translate(${totalOffset.x}, ${totalOffset.y})"
        val transform = "transform=\"$rotate $translate\""

        val svg = StringBuilder()
        svg.append("<g $transform>\n")
        svg.append("<g transform=\"translate(-28, -12) scale(0.22)\">\n")
        svg.append(waterSVG)
        svg.append("</g>\n")
        svg.append("<text x=\"2\" y=\"0\" $align $font>$value</text>\n")
        svg.append("</g>\n")

        return svg.toString()
    }

    private fun getSpiceSvg(
        value: Int,
        offset: Vector2D,
        isRed: Boolean = false,
    ): String {
        val midpoint = (hexagonVertices[1] + innerHexagonVertices[2]) / 2
        val totalOffset = offset + midpoint

        val font = "font-size=\"20\" font-family=\"Times New Roman\""
        val align = "dominant-baseline=\"middle\" text-anchor=\"middle\""
        val color = if (isRed) "red" else "black"

        val rotate = "rotate(-60,${offset.x},${offset.y})"
        val translate = "translate(${totalOffset.x}, ${totalOffset.y})"
        val transform = "transform=\"$rotate $translate\""

        val svg = StringBuilder()
        svg.append("<g $transform>\n")
        svg.append("<g transform=\"translate(-43, -8.5) scale(0.13)\" fill=\"$color\">\n")
        svg.append(spiceSVG)
        svg.append("</g>\n")
        svg.append("<text x=\"17\" y=\"7.5\" $align $font fill=\"$color\">$value</text>\n")
        svg.append("</g>\n")

        return svg.toString()
    }

    private val weaponOffset = innerHexagonVertices[2]

    private fun getWeaponSvg(
        weapon: Weapon,
        offset: Vector2D,
    ): String {
        var totalOffset = offset + weaponOffset
        return when (weapon) {
            Weapon.PISTOL -> {
                "<use href=\"#pistol\" x=\"${totalOffset.x}\" y=\"${totalOffset.y}\" />\n"
            }

            Weapon.LASGUN -> {
                "<use href=\"#lasgun\" x=\"${totalOffset.x}\" y=\"${totalOffset.y}\" />\n"
            }

            Weapon.CRYSKNIFE -> {
                totalOffset += Vector2D(0.0, 3.0)
                "<use href=\"#crysknife\" x=\"${totalOffset.x}\" y=\"${totalOffset.y}\" />\n"
            }

            Weapon.LEGION -> {
                ""
            }
        }
    }

    private fun getPlayerMarkerSvg(
        offset: Vector2D,
        text: String,
        color: String = "black",
    ): String {
        val textPosition = "x=\"${offset.x}\" y=\"${offset.y}\""
        val circlePosition = "cx=\"${offset.x}\" cy=\"${offset.y}\""
        val align = "dominant-baseline=\"middle\" text-anchor=\"middle\""
        val font = "font-size=\"30\""

        val circle = "<circle $circlePosition r=\"25\" fill=\"$color\" />\n"
        val text = "<text $textPosition $align $font fill=\"white\">$text</text>\n"
        return circle + text
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

            val fieldBuilder = StringBuilder()

            fieldBuilder.append(getBackgroundSvg(field.water < 0, offset))

            fieldBuilder.append(getHexagonSvg(offset))
            fieldBuilder.append(getWeaponBoxSvg(offset))
            fieldBuilder.append(getCoordinateCircleSvg(offset))
            fieldBuilder.append(getFieldTextSvg(field.id, offset))
            fieldBuilder.append(getWaterBoxSvg(offset))
            fieldBuilder.append(getSpiceBoxSvg(offset))
            fieldBuilder.append(getWaterSvg(field.water, offset))
            fieldBuilder.append(getSpiceSvg(field.spice, offset))
            fieldBuilder.append(getWeaponSvg(field.effectiveWeapon, offset))

            fieldBuilder.toString()
        }

        val width = maxX - minX + 300
        val height = maxY - minY + 300

        val viewbox = "viewBox=\"${minX - 150} ${minY - 150} $width $height\""

        val svgOpening = "<svg width=\"$width\" height=\"$height\" $viewbox " +
            "xmlns=\"http://www.w3.org/2000/svg\">\n"

        val definitions = getDefinitions()

        val svgEnd = "</svg>"
        return svgOpening + definitions + fieldSvgs.joinToString(separator = "") + svgEnd
    }

    private const val GREEK_ALPHABET = "αβγδεζηθικλμνξοπρστυφχψω"
    private val COLORS = listOf(
        "tomato",
        "steelblue",
        "mediumseagreen",
        "goldenrod",
        "orchid",
        "coral",
        "slateblue",
        "darkcyan",
        "crimson",
        "peru",
        "teal",
        "mediumvioletred",
    )

    fun getGameStateSvg(gameState: GameState): String {
        var minX = 0.0
        var maxX = 0.0
        var minY = 0.0
        var maxY = 0.0

        val fieldSvgs = gameState.fields.map { field ->
            val offset = getOffsetVector(field.id)

            minX = minOf(minX, offset.x)
            maxX = maxOf(maxX, offset.x)
            minY = minOf(minY, offset.y)
            maxY = maxOf(maxY, offset.y)

            val fieldBuilder = StringBuilder()

            fieldBuilder.append(getBackgroundSvg(field.water < 0, offset))

            fieldBuilder.append(getHexagonSvg(offset))
            fieldBuilder.append(getWeaponBoxSvg(offset))
            fieldBuilder.append(getCoordinateCircleSvg(offset))
            fieldBuilder.append(getFieldTextSvg(field.id, offset))
            fieldBuilder.append(getWaterBoxSvg(offset))
            fieldBuilder.append(getSpiceBoxSvg(offset))
            fieldBuilder.append(getWaterSvg(field.water, offset))
            fieldBuilder.append(getSpiceSvg(field.spice, offset, field.harvester))
            fieldBuilder.append(getWeaponSvg(field.effectiveWeapon, offset))

            fieldBuilder.toString()
        }

        val playerSvgs = gameState.players.map { player ->
            val ownedFieldsSvgs = player.ownedFields.map { field ->
                val offset = getOffsetVector(field.id)
                getPlayerMarkerSvg(
                    offset,
                    GREEK_ALPHABET[player.id].toString(),
                    COLORS[player.id],
                )
            }
            ownedFieldsSvgs.joinToString(separator = "")
        }

        val width = maxX - minX + 300
        val height = maxY - minY + 300

        val viewbox = "viewBox=\"${minX - 150} ${minY - 150} $width $height\""

        val svgOpening = "<svg width=\"$width\" height=\"$height\" $viewbox " +
            "xmlns=\"http://www.w3.org/2000/svg\">\n"

        val definitions = getDefinitions()

        val svgEnd = "</svg>"
        return svgOpening + definitions + fieldSvgs.joinToString(separator = "") +
            playerSvgs.joinToString(separator = "") +
            svgEnd
    }
}

suspend fun main() {
//    val desert = Desert.create12PlayerHexagon()

    val a1 = GameStateField(
        id = "A1",
        water = -3,
        spice = 28,
        effectiveWeapon = Weapon.PISTOL,
        neighbours = mutableSetOf(),
        harvester = true,
    )
    val a2 = GameStateField(
        id = "A2",
        water = 7,
        spice = 7,
        effectiveWeapon = Weapon.CRYSKNIFE,
        neighbours = mutableSetOf(),
        harvester = false,
    )
    val b1 = GameStateField(
        id = "B1",
        water = -10,
        spice = 0,
        effectiveWeapon = Weapon.LASGUN,
        neighbours = mutableSetOf(),
        harvester = false,
    )
    val b2 = GameStateField(
        id = "B2",
        water = 11,
        spice = 11,
        effectiveWeapon = Weapon.CRYSKNIFE,
        neighbours = mutableSetOf(),
        harvester = false,
    )

    val gameState = GameState(
        fields = setOf(
            a1,
            a2,
            b1,
            b2,
        ),
        players = setOf(
            Player(
                id = 0,
                water = 12,
                spice = 12,
                harvestersPurchased = 1,
                weapons = mutableMapOf(),
                ownedFields = mutableSetOf(a1, a2),
            ),
            Player(
                id = 1,
                water = 12,
                spice = 12,
                harvestersPurchased = 1,
                weapons = mutableMapOf(),
                ownedFields = mutableSetOf(b1, b2),
            ),
        ),
        id = -1,
        gameId = -1,
        index = -1,
    )

    val fields = (1..12).map {
        GameStateField(
            id = (('A'.code + (it - 1) / 3).toChar().toString() + ((it - 1) % 3 + 1).toString()),
            0,
            0,
            Weapon.PISTOL,
            false,
            mutableSetOf(),
        )
    }
    val gameState2 = GameState(
        fields = fields.toMutableSet(),
        players = fields.mapIndexed { index, field ->
            Player(
                id = index,
                water = 12,
                spice = 12,
                harvestersPurchased = 1,
                weapons = mutableMapOf(),
                ownedFields = mutableSetOf(field),
            )
        }.toMutableSet(),
        id = -1,
        gameId = -1,
        index = -1,
    )

    MapDrawer.loadResources()

    println(MapDrawer.getGameStateSvg(gameState2))
}
