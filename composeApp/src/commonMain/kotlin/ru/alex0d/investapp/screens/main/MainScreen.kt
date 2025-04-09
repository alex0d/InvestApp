@file:OptIn(ExperimentalAdaptiveApi::class)

package ru.alex0d.investapp.screens.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBarItem
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import ru.alex0d.investapp.screens.portfolio.PortfolioScreen
import ru.alex0d.investapp.screens.profile.ProfileScreen
import ru.alex0d.investapp.screens.search.SearchScreen
import ru.alex0d.investapp.ui.composables.CurrentTabX

class MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(PortfolioScreen(), disposeNestedNavigators = false) {
            Scaffold(
                bottomBar = {
                    AdaptiveNavigationBar {
                        TabNavigationItem(PortfolioScreen())
                        TabNavigationItem(SearchScreen())
                        TabNavigationItem(ProfileScreen())
                    }
                },
            ) { innerPadding ->
                CurrentTabX(innerPadding)
            }
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current
        println(tabNavigator)
        println(tabNavigator.current)

        AdaptiveNavigationBarItem(
            selected = tabNavigator.current::class == tab::class,
            onClick = { tabNavigator.current = tab },
            label = { Text(text = tab.options.title) },
            icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
        )
    }
}
