package ru.alex0d.investapp.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.TabOptions
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.account_circle
import investapp.composeapp.generated.resources.portfolio
import investapp.composeapp.generated.resources.profile
import investapp.composeapp.generated.resources.search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class MainTabs(
    val titleRes: StringResource? = null,
    val iconRes: DrawableResource? = null
) {
    PORTFOLIO(
        titleRes = Res.string.portfolio,
        iconRes = Res.drawable.portfolio
    ),
    SEARCH(
        titleRes = Res.string.search,
        iconRes = Res.drawable.search
    ),
    PROFILE(
        titleRes = Res.string.profile,
        iconRes = Res.drawable.account_circle
    ),

    STOCK_DETAILS,
    ORDER,
    TAROT,
}

val MainTabs.voyagerTabOptions: TabOptions
    @Composable
    get() {
        val index = this.ordinal.toUShort()
        val title = this.titleRes?.let { stringResource(it) } ?: ""
        val icon = this.iconRes?.let { painterResource(it) }
        return remember {
            TabOptions(
                index = index,
                title = title,
                icon = icon,
            )
        }
    }
