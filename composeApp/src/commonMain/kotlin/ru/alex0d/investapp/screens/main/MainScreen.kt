@file:OptIn(ExperimentalAdaptiveApi::class)

package ru.alex0d.investapp.screens.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBarItem
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlinx.coroutines.delay
import ru.alex0d.investapp.getPlatform
import ru.alex0d.investapp.screens.portfolio.PortfolioScreen
import ru.alex0d.investapp.screens.profile.ProfileScreen
import ru.alex0d.investapp.screens.search.SearchScreen
import ru.alex0d.investapp.ui.composables.CurrentTabX
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MainScreen : Screen {
    @OptIn(ExperimentalUuidApi::class)
    @Composable
    override fun Content() {
        /**
         * It's a hack to force recompose in Web
         * Without recomposition there are no icons and subtitles in navigation items
         */
        var bottomKey by remember { mutableStateOf(Uuid.random()) }
        if ("Web" in getPlatform().name) {
            LaunchedEffect(Unit) {
                delay(1)
                bottomKey = Uuid.random()
            }
        }

        TabNavigator(PortfolioScreen(), disposeNestedNavigators = false) {
            Scaffold(
                bottomBar = {
                    key(bottomKey) {
                        AdaptiveNavigationBar {
                            TabNavigationItem(PortfolioScreen())
                            TabNavigationItem(SearchScreen())
                            TabNavigationItem(ProfileScreen())
                        }
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
