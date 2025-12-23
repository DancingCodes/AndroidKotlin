package love.moonc.androidkotlin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// @Composable：声明这是一个界面配置函数。
@Composable
fun AndroidKotlinTheme(
    // 参数 1：是否开启深色模式。默认值由系统当前状态决定（isSystemInDarkTheme()）。
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 参数 2：是否开启动态配色（Android 12+ 的功能，App 颜色会随手机壁纸颜色变化）。
    dynamicColor: Boolean = true,
    // 参数 3：重点！这是要被“套上皮肤”的内容。
    // 类型 () -> Unit 表示它是一个代码块，也就是你在 MainActivity 里写在大括号内部的内容。
    content: @Composable () -> Unit
) {
    // 逻辑开始：根据条件选择一套“配色方案” (colorScheme)
    val colorScheme = when {
        // 条件 A：如果开启了动态配色，且手机系统版本 >= Android 12 (SDK 31)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // 根据系统深色/浅色模式，从手机系统提取壁纸颜色作为配色
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // 条件 B：如果是旧手机或关闭了动态配色，且是深色模式
        darkTheme -> DarkColorScheme
        // 条件 C：其他情况（普通浅色模式）
        else -> LightColorScheme
    }

    // MaterialTheme：这是官方的“总管”组件。
    // 它把选好的颜色、字体（Typography）应用到整个 content 中。
    MaterialTheme(
        colorScheme = colorScheme, // 应用上面选出的配色方案
        typography = Typography,   // 应用统一的字体样式
        content = content          // 渲染你传进来的那些 UI 零件
    )
}