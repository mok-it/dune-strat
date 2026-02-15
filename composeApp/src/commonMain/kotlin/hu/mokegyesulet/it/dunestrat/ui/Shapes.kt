package hu.mokegyesulet.it.dunestrat.ui


import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val customShape = GenericShape{ size, _ ->
    val width = size.width
    val height = size.height

    lineTo(0f, 0f)
    lineTo(width, 0f)
    lineTo(width, height*2)
    lineTo(0f, height)
}
val shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)
