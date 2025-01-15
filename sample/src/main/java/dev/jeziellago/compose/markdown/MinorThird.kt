package dev.jeziellago.compose.markdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val fonts = FontFamily(
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.ExtraLight),
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.ExtraLight, FontStyle.Italic),

    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Light),
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Light, FontStyle.Italic),

    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Normal),
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Normal, FontStyle.Italic),

    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Medium),
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Medium, FontStyle.Italic),

    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.SemiBold),
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.SemiBold, FontStyle.Italic),

    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Bold),
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Bold, FontStyle.Italic),

    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Black),
    Font(dev.jeziellago.compose.markdown.sample.R.font.opensans_regular, FontWeight.Black, FontStyle.Italic)
)

object FontScaleHolder {
    var fontScale: Float = 1f

    fun getScaleFactor(): Float {
        return fontScale
    }
}

object MinorThird {
    val h1: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 39.81.sp * FontScaleHolder.getScaleFactor(),
        lineHeight = 47.77.sp,
        color = Color(0xFF0D0235),
    )

    val h2: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 33.18.sp,
        lineHeight = 43.13.sp,
        color = Color(0xFF0D0235),
    )

    val h3: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 27.65.sp,
        lineHeight = 35.94.sp,
        color = Color(0xFF0D0235),
    )

    val subtitle1: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 23.04.sp,
        lineHeight = 29.95.sp,
        color = Color(0xFF0D0235),
    )

    val subtitle2: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Color(0xFF0D0235),
    )

    val subtitle3: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 13.3.sp,
        lineHeight = 20.sp,
        color = Color(0xFF0D0235),
    )

    val link: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Black,
        fontSize = 13.33.sp,
        lineHeight = 20.sp,
        color = Color(0xFF0D0235),
    )

    val body: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 19.20.sp,
        lineHeight = 28.8.sp,
        color = Color(0xFF0D0235),
    )

    val bodyEmphasis: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Black,
        fontSize = 19.20.sp,
        lineHeight = 28.8.sp,
        color = Color(0xFF0D0235),
    )

    val bodyRegular: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 19.20.sp,
        lineHeight = 28.8.sp,
        color = Color(0xFF0D0235),
    )

    val bodySmall: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Color(0xFF0D0235),
    )

    val bodySmallEmphasis: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Color(0xFF0D0235),
    )

    val subtext: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 11.11.sp * FontScaleHolder.getScaleFactor(),
        lineHeight = 15.18.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF0D0235),
    )

    val subtextEmphasis: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Black,
        fontSize = 11.11.sp * FontScaleHolder.getScaleFactor(),
        lineHeight = 15.18.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF0D0235),
    )

    val subtextLarge: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 13.33.sp * FontScaleHolder.getScaleFactor(),
        lineHeight = 20.sp,
    )

    val subtextSmall: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp,
    )

    val subtextLargeEmphasis: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Black,
        fontSize = 13.33.sp * FontScaleHolder.getScaleFactor(),
        lineHeight = 20.sp,
        color = Color(0xFF0D0235),
    )

    val glucose: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 57.33.sp * FontScaleHolder.getScaleFactor(),
        lineHeight = 57.33.sp
    )

    val glucoseUnits: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp * FontScaleHolder.getScaleFactor(),
        lineHeight = 16.sp
    )

    val shareSubtitle: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 5.sp
    )

    val shareTypeTitle: TextStyle = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 19.2.sp
    )

    fun editStyle(
        baseStyle: TextStyle,
        fontFamily: FontFamily? = null,
        fontWeight: FontWeight? = null,
        fontSize: TextUnit? = null,
        lineHeight: TextUnit? = null,
        color: Color? = null,
        textAlign: TextAlign? = null,
        letterSpacing: TextUnit? = null
    ) = baseStyle.copy(
        fontFamily = fontFamily ?: baseStyle.fontFamily,
        fontWeight = fontWeight ?: baseStyle.fontWeight,
        fontSize = fontSize ?: baseStyle.fontSize,
        lineHeight = lineHeight ?: baseStyle.lineHeight,
        color = color ?: baseStyle.color,
        textAlign = textAlign ?: baseStyle.textAlign,
        letterSpacing = letterSpacing ?: baseStyle.letterSpacing
    )
}