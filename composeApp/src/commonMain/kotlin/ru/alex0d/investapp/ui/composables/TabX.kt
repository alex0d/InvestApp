package ru.alex0d.investapp.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import ru.alex0d.investapp.screens.portfolio.PortfolioScreen

@OptIn(InternalVoyagerApi::class)
@Composable
fun CurrentTabX(
    innerPadding: PaddingValues,
) {
    val navigator = LocalNavigator.current
    val tabNavigator = LocalTabNavigator.current
    val currentTab = tabNavigator.current as TabX

    val backHandlerEnabled = navigator?.canPop == true || currentTab !is PortfolioScreen
    tabNavigator.saveableState("currentTab") {
        BackHandler(backHandlerEnabled) {
            if (navigator?.canPop == true) {
                navigator.pop()
            } else {
                navigator?.replaceAll(PortfolioScreen())
            }
        }
        currentTab.Content(innerPadding)
    }
}

interface TabX : Tab {

    @Composable
    fun Content(
        innerPadding: PaddingValues,
    )

    @Composable
    override fun Content() {
        error("Called Content without arguments")
    }
}
