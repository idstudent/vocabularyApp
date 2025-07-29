package com.ljyVoca.vocabularyapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.ljyVoca.vocabularyapp.R

sealed class NaviItem(
    val title: Int,
    val icon: ImageVector,
    val route: String
) {
    data object Home: NaviItem(R.string.home, Icons.Default.Home, "HOME")
    data object Voca: NaviItem(R.string.vocabulary, Icons.Default.Book, "VOCA")
    data object Calendar: NaviItem(R.string.calendar, Icons.Default.CalendarMonth, "CALENDAR")


    companion object {
        private val BOTTOM_NAV_ROTUES = listOf("HOME", "VOCA", "CALENDAR")

        fun showBottomBar(route: String?): Boolean {
            return when {
                route == null -> true
                BOTTOM_NAV_ROTUES.contains(route) -> true
                else -> false
            }
        }
    }
}