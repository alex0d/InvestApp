package ru.alex0d.investapp.screens.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.alex0d.investapp.R

enum class MainTabs(
    @StringRes val titleRes: Int? = null,
    @DrawableRes val iconRes: Int? = null
) {
    PORTFOLIO(
        titleRes = R.string.portfolio,
        iconRes = R.drawable.portfolio
    ),
    SEARCH(
        titleRes = R.string.search,
        iconRes = R.drawable.search
    ),
    PROFILE(
        titleRes = R.string.profile,
        iconRes = R.drawable.account_circle
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
