package com.ljyVoca.vocabularyapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ljyVoca.vocabularyapp.R
import androidx.compose.ui.text.PlatformTextStyle

private val WantedRegular = FontFamily(Font(R.font.wanted_regular))
private val WantedExtraBold = FontFamily(Font(R.font.wanted_extra_bold))
private val WantedSemiBold = FontFamily(Font(R.font.wanted_semi_bold))

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = WantedRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)


object AppTypography {
    val fontSize14Regular = TextStyle(
        fontFamily = WantedRegular,
        fontSize = 14.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val fontSize16Regular = TextStyle(
        fontFamily = WantedRegular,
        fontSize = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val fontSize20Regular = TextStyle(
        fontFamily = WantedRegular,
        fontSize = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )


    val fontSize14SemiBold = TextStyle(
        fontFamily = WantedSemiBold,
        fontSize = 14.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val fontSize16SemiBold = TextStyle(
        fontFamily = WantedSemiBold,
        fontSize = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val fontSize20SemiBold = TextStyle(
        fontFamily = WantedSemiBold,
        fontSize = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val fontSize16ExtraBold = TextStyle(
        fontFamily = WantedExtraBold,
        fontSize = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val fontSize20ExtraBold = TextStyle(
        fontFamily = WantedExtraBold,
        fontSize = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val fontSize24ExtraBold = TextStyle(
        fontFamily = WantedExtraBold,
        fontSize = 24.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )
}