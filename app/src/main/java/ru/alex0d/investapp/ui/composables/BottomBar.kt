package ru.alex0d.investapp.ui.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.PortfolioScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import com.ramcosta.composedestinations.utils.startDestination
import ru.alex0d.investapp.R

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val navigator = navController.rememberDestinationsNavigator()
    NavigationBar {
        val currentDestination: DestinationSpec = navController.currentDestinationAsState().value
            ?: NavGraphs.main.startDestination

        BottomBarItem.entries.forEach { barItem ->
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(barItem.direction)
            val selected = currentDestination.route == barItem.direction.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        navigator.popBackStack(barItem.direction, false)
                        return@NavigationBarItem
                    }

                    navigator.navigate(barItem.direction) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(barItem.icon),
                        contentDescription = stringResource(barItem.label),
                        tint = if (selected) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = { Text(stringResource(barItem.label)) },
            )
        }
    }
}

enum class BottomBarItem(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    Portfolio(PortfolioScreenDestination, R.drawable.portfolio, R.string.portfolio),
    Profile(ProfileScreenDestination, R.drawable.account_circle, R.string.profile)
}