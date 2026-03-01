package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val hexagonalRectangle = GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    lineTo(0f, height * 0.9f)
    lineTo(width / 2, height)
    lineTo(width, height * 0.9f)
    lineTo(width, height * 0.1f)
    lineTo(width / 2, 0f)
    lineTo(0f, height * 0.1f)
}

val fancyRectangle = GenericShape { size, _ ->
    val width = size.width
    val height = size.height
    val fancyBitSize = 5f

    lineTo(0f, height)
    lineTo(width / 2 - fancyBitSize, height)
    lineTo(width / 2, height + fancyBitSize)
    lineTo(width / 2 + fancyBitSize, height)
    lineTo(width, height)
    lineTo(width, 0f)
    lineTo(width / 2 + fancyBitSize, 0f)
    lineTo(width / 2, -fancyBitSize)
    lineTo(width / 2 - fancyBitSize, 0f)
    lineTo(0f, 0f)
}
val shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)
