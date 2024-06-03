package the.hart.repositte.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    tertiary = Pink80,
    primary = PrimaryDark,
    secondary = Secondary,
    background = BackgroundDark,
    onPrimary = OnPrimaryDark,
    onSecondary = OnSecondaryDark,
    onBackground = OnBackgroundDark,
)

private val LightColorScheme = lightColorScheme(
    tertiary = Pink80,
    primary = PrimaryLight,
    secondary = Secondary,
    background = BackgroundLight,
    onPrimary = OnPrimaryLight,
    onSecondary = OnSecondaryLight,
    onBackground = OnBackgroundLight,
)

@Composable
fun RepositteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
    val colorScheme = LightColorScheme
    // todo ::

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}