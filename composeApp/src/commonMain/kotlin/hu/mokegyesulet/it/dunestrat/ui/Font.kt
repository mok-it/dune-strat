package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dune_strat.composeapp.generated.resources.Dune_Rise
import dune_strat.composeapp.generated.resources.Res
import dune_strat.composeapp.generated.resources.metropolis_bold
import dune_strat.composeapp.generated.resources.metropolis_light
import dune_strat.composeapp.generated.resources.metropolis_medium
import dune_strat.composeapp.generated.resources.metropolis_normal
import dune_strat.composeapp.generated.resources.metropolis_semi_bold
import org.jetbrains.compose.resources.Font

@Composable
fun metropolisFontFamily() = FontFamily(
    Font(Res.font.metropolis_light, weight = FontWeight.Light),
    Font(Res.font.metropolis_normal, weight = FontWeight.Normal),
    Font(Res.font.metropolis_medium, weight = FontWeight.Medium),
    Font(Res.font.metropolis_semi_bold, weight = FontWeight.SemiBold),
    Font(Res.font.metropolis_bold, weight = FontWeight.Bold),
)

@Composable
fun duneFontFamily() = FontFamily(
    Font(Res.font.Dune_Rise),
)

@Composable
fun metropolisTypography() = Typography().run {
    val fontFamily = metropolisFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily),
    )
}
