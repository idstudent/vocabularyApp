package com.ljyVoca.vocabularyapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NaviItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    data object Home: NaviItem("HOME", Icons.Default.Home, "HOME")
    data object Search: NaviItem("SEARCH", Icons.Default.Search, "SEARCH")
    data object Cart: NaviItem("MY CART", Icons.Default.ShoppingCart, "MY CART")
    data object Profile: NaviItem("PROFILE", Icons.Default.Person, "PROFILE")

    companion object {
        private val BOTTOM_NAV_ROTUES = listOf("HOME", "SEARCH", "MY CART", "PROFILE")

        fun showBottomBar(route: String?): Boolean {
            return when {
                route == null -> true
                BOTTOM_NAV_ROTUES.contains(route) -> true
                else -> false
            }
        }
    }
}